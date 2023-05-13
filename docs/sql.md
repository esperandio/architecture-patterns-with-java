```sql
CREATE TABLE test.song (
	songId INT auto_increment NOT NULL,
	singer varchar(100) NULL,
	songName varchar(100) NULL,
	CONSTRAINT song_PK PRIMARY KEY (songId)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;
```