import React, {useMemo} from "react";

export default function LoginPage() {
  const href = useMemo(() => {
    let port = (window.location.port ? ':' + window.location.port : '');
    // handle localhost dev case
    if (port === ':3000') {
      port = ':8080';
    }
    return window.location.protocol + '//' + window.location.hostname + port + '/oauth2/authorization/google';
  }, []);

  return <div id="login-container" className={"content-rounded-border-box content"}>
    <h1>Please log in to continue</h1>
    <a href={href} className="content-rounded-border-box content-inner">
      <span>Log in with Google (OAuth2)</span>
    </a>
  </div>
      ;
}