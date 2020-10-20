# User Story
1. 장소를 찾기 위해 키워드(keyword 2\~50자)와 페이지번호(pageNo 1\~45)을  입력한다.
```
curl GET 'http://localhost:8080/v1/place?keyword=%EC%82%BC%EA%B2%B9%EC%82%B4&pageNo=1'
```
2. 검색된 결과에는 키워드와 관련된 장소의 상호명들과 각 상호명에 대한 3개의 이미지가 노출된다.
3. 검색된 결과를 보고 다음 페이지 번호로 조회한다.
```
curl GET 'http://localhost:8080/v1/place?keyword=%EC%82%BC%EA%B2%B9%EC%82%B4&pageNo=2'
```
4. 다른 10가지 키워드도 각각 여러 번 조회를 시도한다.
```
curl GET 'http://localhost:8080/v1/place?keyword=%EA%B0%90%EC%9E%90%ED%83%95&pageNo=1'
curl GET 'http://localhost:8080/v1/place?keyword=%ED%9A%9F%EC%A7%91&pageNo=1'
curl GET 'http://localhost:8080/v1/place?keyword=%ED%8E%B8%EC%9D%98%EC%A0%90&pageNo=1'
curl GET 'http://localhost:8080/v1/place?keyword=%EC%B9%B4%ED%8E%98&pageNo=1'
curl GET 'http://localhost:8080/v1/place?keyword=%EB%B2%A0%EC%9D%B4%EC%BB%A4%EB%A6%AC&pageNo=1'
curl GET 'http://localhost:8080/v1/place?keyword=%EB%8C%80%ED%95%99%EA%B5%90&pageNo=1'
curl GET 'http://localhost:8080/v1/place?keyword=%EA%B3%B5%EC%9B%90&pageNo=1'
curl GET 'http://localhost:8080/v1/place?keyword=%EA%B4%80%EA%B4%91%EC%A7%80&pageNo=1'
curl GET 'http://localhost:8080/v1/place?keyword=%EB%8B%AD%EA%B0%88%EB%B9%84&pageNo=1'
curl GET 'http://localhost:8080/v1/place?keyword=%EC%96%91%EA%B0%88%EB%B9%84&pageNo=1'
```
5. 총 11가지 키워드로 여러 번 호출하였을 때 조회 수 상위 10개를 조회한다.
```
curl GET 'http://localhost:8080/v1/place/rank'
```
6. 조회된 결과는 조회 횟수가 높은 순서로 정렬되며 키워드도 같이 확인 할 수 있다.

# 비기능 요구사항
* 신뢰성
  * 검색 기능이 유효하다면 키워드 조회 횟수 저장으로 부터 발생하는 오류는 무시한다.
  * 검색 횟수 증가로 인한 횟수 저장에 병목이 발생하는 경우 로컬캐싱하여 주기적으로 DB에 저장하여 병목을 완화한다.
  * 검색 기능이 유효하다면 부가적인 이미지 조회는 오류가 발생하더라도 무시한다.
  * 외부의 검색 API를 활용하였을 때 차선책의 API로 시도하여 검색결과를 반환환다.
  * 외부의 검색 API의 응답지연으로 인하여 전체 성능저하의 우려가 있음으로 응답을 받기까지의 timeout을 정한다.
  * 주요검색 API와 차선의 검색API 모두 장애가 발생할 경우를 대비하여 하루정도의 검색결과를 별도로 캐시서버에 캐싱하여 대응한다.
  * 대량의 요청을 받을 경우 확장을 위해서 서버간 공유를 위한 상태 값은 캐시서버나 DB에서 관리한다.
  * 상위 10개의 키워드 API의 부하가 클 경우 저장된 DB를 readonly용으로 replication 구성한다.
  * 외부의 검색 API를 사용하는 경우에는 blocking 되었을 경우 영향이 적도록 비동기 호출을 한다.
  * API서버를 제외한 전체 장애가 발생하였을 경우에는 처리 대기 중인 Thread가 누적되지 않도록 Fast Fail 기준과 대안도 마련한다.
    * 외부검색 API 호출 timeout설정
    * DBCP fast fail option 설정
    
# 사용 Opensource
* unirest-java : HTTP client 용도
* jackson-core : JSON parser 용도
* lombok : Data Object 코드비용 절감목적
