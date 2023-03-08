CREATE TABLE board(
     seq NUMBER NOT NULL,               -- 글번호 (시퀀스 객체 이용)
     id VARCHAR2(20) NOT NULL,           -- 아이디
     name VARCHAR2(40) NOT NULL,       -- 이름
     email VARCHAR2(40),                  -- 이메일
     subject VARCHAR2(255) NOT NULL,    -- 제목
     content VARCHAR2(4000) NOT NULL,   -- 내용 

     ref NUMBER NOT NULL,                 -- 그룹번호 seq와 같은 번호
     lev NUMBER DEFAULT 0 NOT NULL,     -- 단계  
     step NUMBER DEFAULT 0 NOT NULL,    -- 글순서 (그룹번호가 같을 경우 +1) 0일 경우 원본
     pseq NUMBER DEFAULT 0 NOT NULL,    -- 원글번호 (답변 달 때 원본 글의 seq) 0일 경우 원본
     reply NUMBER DEFAULT 0 NOT NULL,   -- 답변수 

     hit NUMBER DEFAULT 0,              -- 조회수
     logtime DATE DEFAULT SYSDATE
 );

 -- 글 list 뽑아 올 때, ref desc, step asc (댓글이랑 원글 그룹 번호로 묶여서 나올 수 있도록)

CREATE SEQUENCE seq_board  NOCACHE NOCYCLE;