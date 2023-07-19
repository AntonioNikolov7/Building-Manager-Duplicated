import { GoogleLogin } from "@react-oauth/google";

const GoogleButton = () => {
  const postCredentials = async (credentials) => {
    console.log(credentials.credential);
    const response = await fetch(
      "http://localhost:8080/login/oauth2/code/google",
      {
        method: "POST",
        credentials: "include",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(credentials.credential),
      }
    );

    if (response.ok) {
      const jsonResponse = await response.json();
      console.log(jsonResponse);
    } else {
      console.log("Error posting credentials");
    }
  };

  return (
    <GoogleLogin
      onSuccess={(credentialResponse) => {
        console.log(credentialResponse);
        postCredentials(credentialResponse);
      }}
      onError={() => {
        console.log("Login Failed");
      }}
    />
  );
};

export default GoogleButton;
