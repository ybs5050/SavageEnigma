CREATE TABLE "decrypted_text" (
    "ID" INT NOT NULL GENERATED ALWAYS AS IDENTITY 
                (START WITH 1, INCREMENT BY 1),
    "ENCRYPTED_ID" INT NOT NULL REFERENCES app."encrypted_text"(ID),
    "DECRYPTED_TEXT" VARCHAR(250) NOT NULL,
    "STATUS" BOOLEAN DEFAULT FALSE NOT NULL,
    "DATE_ENTERED" VARCHAR(10) NOT NULL,
    PRIMARY KEY (ID)
);