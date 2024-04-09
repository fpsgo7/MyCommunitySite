/* 스프링 부트 JPA는 자체적으로 엔티티 클래스의 자동증가 규칙을 인색해서 처리해주지만
   h2 데이터베이스는 데이터베이스가 생성되고 외래키까지는 알아서 해주지만 자동 증가 는 적용되지 않아
   여기서 테이블을 설정해줘야한다.*/
/* h2 데이터베이스는 시퀸스 방식을 사용하기 때문에 id 값을 시퀸스로 작성해줘야한다.
    예 : next value for members_seq 단 해당 방식은 id 값이  50씩 증가해 여기서 2개의 데이터를 출력하면
    1, 51 이 id 값으로 지정된다 테스트에는 문제가 없으나 실사용은 하면안된다. */
insert into members (member_id,email,password,roles) values (next value for members_seq,'1@1','$2a$10$cyVvBSgLpuz43Lz4pSN7F.sjMhRk8b4VEMvaavLIBrciVgb0jLuli','ROLE_MANAGER,');
insert into members (member_id,email,password,roles) values (next value for members_seq,'1@2','$2a$10$cyVvBSgLpuz43Lz4pSN7F.sjMhRk8b4VEMvaavLIBrciVgb0jLuli','ROLE_USER,');