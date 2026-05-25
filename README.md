# liveklass

liveklass 채용 과제 제출용 레포지토리입니다.

## 1. 실행 방법

### 필요한 도구
- JDK 21
- Docker Desktop

### 설치 및 실행 순서
1. JDK 21을 설치합니다.
2. Docker Desktop을 실행합니다.
3. 프로젝트 루트에서 의존 서비스와 애플리케이션을 실행합니다.

```bash
./gradlew bootRun
docker compose up --build -d
```

Windows 환경에서는 아래 명령을 사용합니다.

```powershell
gradlew.bat bootRun
docker compose up --build -d
```

애플리케이션은 `8082` 포트에서 실행되며, MySQL과 Kafka 설정은 `src/main/resources/application.yaml` 기준으로 동작합니다.

데이터 시각화는 Grafana를 사용해서 `localhost:3000`으로 접속해서 ID: admin, PassWord: admin으로 접속해서 차트를 생성할 수 있습니다.
## 2. 스키마 설명

`event_logs`는 개별 이벤트 로그 원본을 저장하기 위한 테이블입니다. 이벤트 종류에 따라 필요한 컬럼만 채우고 나머지는 비워 둘 수 있게 설계해서, 다양한 이벤트 타입을 한 테이블에서 유연하게 다룰 수 있도록 했습니다.

`page_view_statistics`는 일별 페이지 방문 집계를 저장하기 위한 테이블입니다. `page_url + statistic_date`에 유니크 키를 두어 중복 집계를 막고, 하루 동안 가장 많이 방문한 URL을 빠르게 조회할 수 있게 했습니다.

`search_keyword_statistics`는 시간별 검색어 집계를 저장하기 위한 테이블입니다. `search_keyword + statistic_hour`에 유니크 키를 두어 검색어 트렌드를 빠르게 확인할 수 있게 했습니다.

## 3. 구현하면서 고민한 점

### 데이터 삽입 및 읽기

처음에는 `event_logs` 테이블 하나에 모든 이벤트 로그를 저장한 뒤, `GROUP BY`나 `WHERE` 조건을 활용해 필요한 데이터를 직접 집계하는 구조로 설계했습니다. 하지만 데이터가 점점 증가하면서 조회 시 대량의 데이터를 스캔해야 했고, 읽기 성능이 점차 저하되는 문제가 발생했습니다.

이러한 시행착오를 겪으며 읽기와 쓰기의 역할을 분리하는 방향으로 구조를 개선했습니다.

읽기 성능을 개선하기 위해 집계 테이블을 별도로 구성하여 미리 가공된 데이터를 관리하도록 했고, 이를 통해 복잡한 집계 쿼리 없이도 빠르게 데이터를 조회할 수 있도록 최적화했습니다.

또한 쓰기 성능과 안정성을 높이기 위해 `Kafka`를 도입했습니다. 이벤트가 한 번에 대량으로 유입되더라도 Kafka가 버퍼 역할을 수행해 서버에 직접적인 부하가 몰리지 않도록 했으며, 비동기적으로 데이터를 처리하여 안정적인 저장과 함께 처리 속도 또한 향상시킬 수 있었습니다.

---

### K8s와 Docker Compose

프로젝트는 Java 기반의 `Spring Boot` 환경에서 `Kafka Producer`와 `Kafka Consumer`를 분리하여, Producer가 생성한 이벤트를 Consumer가 비동기적으로 처리하는 구조로 설계했습니다.

초기에는 Producer와 Consumer를 각각 독립된 `JVM`으로 실행하는 형태를 고려했고, 실제로 `Docker Compose`를 활용해 멀티 컨테이너 환경을 구성해보려 했습니다. 하지만 Docker Compose를 처음 다루다 보니 컨테이너 간 네트워크 설정이나 포트 충돌 문제를 반복적으로 겪었고, 여러 JVM을 동시에 관리하는 과정에서 예상보다 많은 시행착오가 발생했습니다.

결국 프로젝트 완성도를 우선적으로 고려해 Producer와 Consumer를 하나의 JVM 내부에서 동작하도록 구조를 단순화했습니다. 비록 최종적으로는 단일 JVM 구조를 선택했지만, 이 과정에서 분산 환경에서의 서비스 관리와 컨테이너 오케스트레이션의 필요성을 직접 체감할 수 있었습니다.

만약 프로젝트를 더 확장할 수 있었다면, Producer와 Consumer를 완전히 분리된 서비스로 운영하고 `Kubernetes(K8s)`를 활용해 컨테이너 배포 및 스케일링, 장애 복구까지 고려한 구조로 발전시켜 보고 싶었습니다. 특히 트래픽 증가 상황에서 Consumer를 수평 확장하거나, Kafka 기반 이벤트 처리 파이프라인을 안정적으로 운영하는 경험까지 이어가고 싶었습니다.

이번 프로젝트를 통해 단순히 기능 구현에만 집중하기보다, 실제 서비스 환경에서는 어떤 구조가 필요한지 고민하고 직접 부딪혀보는 과정 자체가 큰 배움이 되었습니다.

---

### 객체 생성 구조 리팩토링

`EventGenerator`에서 `builder().build().build()` 형태의 중복 호출이 발생하는 문제가 있었습니다.

원인은 `EventRequest` 생성을 위해 중간 가교 역할의 `EventRequestBuilder`를 별도로 두면서, Builder 패턴이 불필요하게 한 단계 더 중첩된 구조가 되었기 때문입니다.

이를 해결하기 위해 `EventRequestBuilder`를 제거하고, `EventRequest` 자체에 Lombok의 `@Builder`를 적용했습니다.

그 결과 객체 생성 흐름이 단순해졌고, 코드 가독성과 유지보수성 또한 개선할 수 있었습니다.

`EventGenerator`에서 `builder().build().build()` 형태의 중복 호출이 발생하는 문제가 있었다.

원인은 `EventRequest` 생성을 위해 중간 가교 역할의 `EventRequestBuilder`를 별도로 두면서, Builder 패턴이 불필요하게 한 단계 더 중첩된 구조가 되었기 때문이다.

이를 해결하기 위해 `EventRequestBuilder`를 제거하고, `EventRequest` 자체에 Lombok의 `@Builder`를 적용했다.  
그 결과 객체 생성 흐름이 단순해졌고, 코드 가독성과 유지보수성 또한 개선할 수 있었다.

