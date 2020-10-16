import React from "react";
import Grid from "@material-ui/core/Grid";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";
import Link from "@material-ui/core/Link";
import PrivacyParagraph from "./PrivacyParagraph";
import List from "@material-ui/core/List";
import ListItem from "@material-ui/core/ListItem";
import ListItemText from "@material-ui/core/ListItemText";
import Card from "@material-ui/core/Card";
import CardContent from "@material-ui/core/CardContent";
import CardHeader from "@material-ui/core/CardHeader";
import CardActionArea from "@material-ui/core/CardActionArea";
import ListItemIcon from "@material-ui/core/ListItemIcon";
import FormatListNumberedOutlinedIcon from '@material-ui/icons/FormatListNumberedOutlined';
import PersonOutlineOutlinedIcon from '@material-ui/icons/PersonOutlineOutlined';
import HttpsOutlinedIcon from '@material-ui/icons/HttpsOutlined';
import VpnLockOutlinedIcon from '@material-ui/icons/VpnLockOutlined';
import RefreshOutlinedIcon from '@material-ui/icons/RefreshOutlined';
import ContactMailOutlinedIcon from '@material-ui/icons/ContactMailOutlined';
import Divider from "@material-ui/core/Divider";

const useStyles = makeStyles((theme: Theme) =>
    createStyles({
      root: {
        height: window.innerHeight - theme.spacing(8),
        overflowY: "scroll",
        overflowX: "hidden"
      },
      list: {
        boxSizing: "border-box",
        scrollbarWidth: "none",
      },
      nav: {
        width: "100%"
      },
      tldr: {
        padding: 0
      },
      tldrInset: {
        '&::before': {
          content: '" "',
          display: "block",
          width: theme.spacing(2),
          minWidth: theme.spacing(2),
        }
      },
    }),
);

export default function PrivacyPolicy() {
  const classes = useStyles();
  return <div className={classes.root}>
    <Grid className={classes.list}
          container
          direction="column"
          justify="center"
          alignContent="center"
          alignItems="center"
          wrap="nowrap">
      <Card className={classes.nav}>
        <CardHeader title="Contents"/>
        <CardContent>
          <List aria-label="contents">
            <Divider/>
            {[
              {title: "tl;dr", icon: <FormatListNumberedOutlinedIcon/>},
              {title: "Welcome to our Privacy Policy", icon: <PersonOutlineOutlinedIcon/>},
              {title: "Security", icon: <HttpsOutlinedIcon/>},
              {title: "Cookies", icon: <VpnLockOutlinedIcon/>},
              {title: "Privacy Policy Changes", icon: <RefreshOutlinedIcon/>},
              {title: "Credit & Contact Information", icon: <ContactMailOutlinedIcon/>},
            ].map(t => <>
              <CardActionArea key={t.title} component="a" href={`#${t.title}`}>
                <ListItem>
                  <ListItemIcon>{t.icon}</ListItemIcon>
                  <ListItemText primary={t.title}/>
                </ListItem>
              </CardActionArea>
              <Divider/>
            </>)}
          </List>
        </CardContent>
      </Card>
      <PrivacyParagraph title="tl;dr">
        <List className={classes.tldr}>
          <ListItem className={classes.tldr}>
            <ListItemText
                primary={<>
                  <span>Cookies are used to identify you and prevent </span>
                  <Link href="https://owasp.org/www-community/attacks/csrf"
                        title="OWASP: Cross Site Request Forgery">
                    XSRF
                  </Link>
                  <span> attacks.</span>
                </>}/>
          </ListItem>
          <ListItem className={classes.tldr}>
            <ListItemText primary="The only data we store about you are:"/>
          </ListItem>
          <ListItem className={classes.tldr + " " + classes.tldrInset}>
            <ListItemText primary="Your unique Google Account ID."
                          secondary="So we can tell who owns each invoice"/>
          </ListItem>
          <ListItem className={classes.tldr + " " + classes.tldrInset}>
            <ListItemText primary="The invoice details and files that you upload"
                          secondary="Until you delete the invoice"/>
          </ListItem>
          <ListItem className={classes.tldr}>
            <ListItemText primary={<>
              <span>Contact my </span>
              <Link href={"mailto:kathan@cheem.uk"}>email</Link>
              <span> if you have any further questions.</span>
            </>}/>
          </ListItem>
        </List>
      </PrivacyParagraph>
      <PrivacyParagraph title="Welcome to our Privacy Policy">
        <>
          It is Invoice Store's policy to respect your privacy regarding any information we may
          collect while operating our website. This Privacy Policy applies to
          <span> </span>
          <Link href="https://invoice.cheem.uk">https://invoice.cheem.uk</Link>
          <span> </span>
          (hereinafter, "us", "we", or "https://invoice.cheem.uk"). We respect your privacy and are
          committed to protecting personally identifiable information you may provide us through the
          Website. We have adopted this privacy policy ("Privacy Policy") to explain what
          information may be collected on our Website, how we use this information, and under what
          circumstances we may disclose the information to third parties. This Privacy Policy
          applies only to information we collect through the Website and does not apply to our
          collection of information from other sources.
        </>
        <>
          This Privacy Policy, together with the Terms of service posted on our Website, set forth
          the general rules and policies governing your use of our Website. Depending on your
          activities when visiting our Website, you may be required to agree to additional terms of
          service.
        </>
      </PrivacyParagraph>

      <PrivacyParagraph title="Security">
        The security of your Personal Information is important to us, but remember that no method of
        transmission over the Internet, or method of electronic storage is 100% secure. While we
        strive to use commercially acceptable means to protect your Personal Information, we cannot
        guarantee its absolute security.
      </PrivacyParagraph>

      <PrivacyParagraph title="Cookies">
        <>
          To enrich and perfect your online experience, Invoice Store uses "Cookies" to display
          personalized content and store your preferences on your computer.
        </>
        <>
          A cookie is a string of information that a website stores on a visitor's computer, and
          that the visitor's browser provides to the website each time the visitor returns. Invoice
          Store uses cookies to help Invoice Store identify visitors and their website access
          preferences. Invoice Store visitors who do not wish to have cookies placed on their
          computers should set their browsers to refuse cookies before using Invoice Store's
          websites, with the drawback that certain features of Invoice Store's websites may not
          function properly without the aid of cookies.
        </>
        <>
          By continuing to navigate our website without changing your cookie settings, you hereby
          acknowledge and agree to Invoice Store's use of cookies.
        </>
      </PrivacyParagraph>

      <PrivacyParagraph title="Privacy Policy Changes">
        Although most changes are likely to be minor, Invoice Store may change its Privacy Policy
        from time to time, and in Invoice Store's sole discretion. Invoice Store encourages visitors
        to frequently check this page for any changes to its Privacy Policy. Your continued use of
        this site after any change in this Privacy Policy will constitute your acceptance of such
        change.
      </PrivacyParagraph>

      <PrivacyParagraph title="Credit & Contact Information">
        <>
          This privacy policy was created at
          <span> </span>
          <Link href="https://privacygenerator.net/privacypolicy/"
                title="Privacy policy template generator"
                target="_blank">privacygenerator.net</Link>
          . If you have
          any questions about this Privacy Policy, please contact us via
          <span> </span>
          <Link href="mailto:kathan@cheem.uk">email</Link>
          .
        </>
      </PrivacyParagraph>
    </Grid>
  </div>;
}