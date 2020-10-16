# invoice store ![CD](https://github.com/cheemcheem/invoice-store/workflows/CD/badge.svg) ![Release](https://github.com/cheemcheem/invoice-store/workflows/Release/badge.svg)

See it live at [invoice.cheem.uk](https://invoice.cheem.uk)

### Usage
1. Run `./mvnw clean package`.
2. Run `java -jar target/invoice-store.jar`.
3. See [Profiles](#profiles) below for more information about running.

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


### Next steps
I hope to add:
1. Categorisation, maybe in the form of searchable tags.
2. Searching/filtering of invoices, by tag, date, total, etc.
3. Exporting filtered data to a CSV.

### Profiles
A list of features, and profiles that can be used to modify them.

| Feature        | Default State (no profile)                                       | Modified State (with profile)                                                          |
| :------------- | :--------------------------------------------------------------- | :-------------------------------------------------------------------------------------- |
| Storage        | Connects to an in-memory S3 Bucket and an in-memory H2 database. | Use `prod` profile to enable connecting to an external AWS S3 Bucket / PostgresQL DB.   |
| Authentication | No authentication.                                               | Use `oauth2` profile to enable OAuth2 authentication using Google.                      |
| Live React     | No adaptations.                                                  | Use `live` profile to enable compatibility with React Scripts running on separate port. |

### Environmental Variables
A list of profiles, and their required environmental variables.

| Profile  | Environmental Variables |
| :------- | :---------------------- |
| `prod`   | <ul><li>`JPA_URL` URL of PostgreSQL Instance</li><li>`JPA_DB` Database to connect to in the PostgreSQL instance</li><li>`JPA_USERNAME` Database username</li><li>`JPA_PASSWORD` Database password</li><li>`AWS_ACCESS_KEY_ID` AWS Access Key ID</li><li>`AWS_SECRET_ACCESS_KEY` AWS Secret Access Key</li><li>`AWS_ENDPOINT` AWS S3 Endpoint</li><li>`AWS_S3_BUCKET` AWS S3 Bucket Name</li><li>`AWS_REGION` AWS Region</li><li>`AWS_S3_BUCKET_ROOT` Key to use as prefix for files stored in S3 Bucket</li><li>`AWS_MAX_FILE_LIMIT` Maximum number of files each user can store</li><li>`DB_MAX_POOL_SIZE` Hikari pool size for database connections</li><li>`DB_MAX_USER_LIMIT` Maximum number of users that can be registered</li></ul> |
| `oauth2` | <ul><li>`GOOGLE_CLIENT_ID` OAuth2 Google Client ID</li><li>`GOOGLE_CLIENT_SECRET` OAuth2 Google Secret</li></ul> |
| other    | <ul><li>`SPRING.PROFILES.ACTIVE` Comma separated list of any of `prod`, `oauth2`, `live`.</li><li>`SERVER_PORT` Port to run application on</li></ul>