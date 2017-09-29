CREATE TABLE "users" (
    "ID" INT NOT NULL GENERATED ALWAYS AS IDENTITY 
                (START WITH 1, INCREMENT BY 1),
    "USERNAME" VARCHAR(30) NOT NULL,
    "PASSWORD" VARCHAR(30) NOT NULL,
    PRIMARY KEY (ID)
);