# invoice store ![CD](https://github.com/cheemcheem/invoice-store/workflows/CD/badge.svg) ![Release](https://github.com/cheemcheem/invoice-store/workflows/Release/badge.svg)

See it live at [invoice.cheem.uk](https://invoice.cheem.uk)

### What this aims to be
An invoice manager. This was created to solve a problem of mine, it can be used to upload documents or photographs, as well as the totals of invoices. You can then download the individual files or a csv file with all of your invoices.

<div style=";width:100%; display:flex; flex-direction:row; flex-wrap:wrap; justify-content:space-between;">
  <img src="/screenshots/login.png?raw=true"     height="275" alt="Screenshot of login page."       />
  <img src="/screenshots/date.png?raw=true"      height="275" alt="Screenshot of date picker."      />
  <img src="/screenshots/form.png?raw=true"      height="275" alt="Screenshot of new invoice form." />
  <img src="/screenshots/uploading.png?raw=true" height="275" alt="Screenshot of uploading invoice."/>
  <img src="/screenshots/created-1.png?raw=true" height="275" alt="Screenshot of a created invoice."/>
  <img src="/screenshots/all.png?raw=true"       height="275" alt="Screenshot of all invoices page."/>
</div>

### Usage
1. Run `./mvnw clean package`
2. Have the following environmental variables available:
    - `JPA_URL` URL of PostgreSQL Instance
    - `JPA_DB` Database to connect to in the PostgreSQL instance
    - `JPA_USERNAME` Database username
    - `JPA_PASSWORD` Database password
    - `DB_MAX_POOL_SIZE` Hikari pool size for database connections
    - `GOOGLE_CLIENT_ID` OAuth2 Google Client ID
    - `GOOGLE_CLIENT_SECRET` OAuth2 Google Secret
    - `AWS_ACCESS_KEY_ID` AWS Access Key ID
    - `AWS_SECRET_ACCESS_KEY` AWS Secret Access Key
    - `AWS_ENDPOINT` AWS S3 Endpoint
    - `AWS_S3_BUCKET` AWS S3 Bucket Name
    - `AWS_REGION` AWS Region
    - `AWS_S3_BUCKET_ROOT` Key to use as prefix for files stored in S3 Bucket
    - `AWS_MAX_FILE_LIMIT` Maximum number of files each user can store
    - `DB_MAX_USER_LIMIT` Maximum number of users that can be registered
    - `SPRING.PROFILES.ACTIVE` Comma separated list of below
        - `prod` for PostgreSQL connection
        - `dev` for H2 in memory database
        - `oauth` enables OAuth2 support
        - `create` will force create the required database schema on the database
        - `debug`enables debug output
        - `debug-spring`enables debug output for spring
        - `debug-all` enables debug output on root logger
    - `SERVER_PORT` Port to run application on
3. Run `java -jar target/invoice-store.jar`

### Next steps
I hope to add:
1. Categorisation, maybe in the form of searchable tags.
2. Searching/filtering of invoices, by tag, date, total, etc.
3. Exporting filtered data to a CSV.
