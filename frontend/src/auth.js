import { UserManager } from 'oidc-client-ts'

const config = {
  authority: 'https://accounts.google.com',
  client_id: '371949021462-ln0r7kjjjdbtofl10ehnufjo7akt99d4.apps.googleusercontent.com',
  redirect_uri: 'http://localhost:5173/callback',
  response_type: 'code',
  scope: 'openid profile email',
  client_secret: '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',
}

const githubConfig = {
  authority: 'https://github.com/',
  client_id: 'Ov23liXqJ9pvGQeU9Ut1',
  redirect_uri: 'http://localhost:5173/github-callback',
  response_type: 'code',
  scope: 'openid profile email',
  client_secret: 'ef09d601c98a19fb71b718b293137203c6990668',
}

const userManager = new UserManager(config)
const githubUserManager = new UserManager(githubConfig)

export default {
  loginWithGoogle() {
    return userManager.signinRedirect()
  },

  loginWithGithub() {
    return githubUserManager.signinRedirect()
  },

  logout() {
    return userManager.signoutRedirect()
  },

  logoutGithub() {
    return githubUserManager.signoutRedirect()
  },

  getUser() {
    return userManager.getUser() || githubUserManager.getUser()
  },

  handleGithubCallback() {
    return githubUserManager.signinRedirectCallback()
  },

  handleGoogleCallback() {
    return userManager.signinRedirectCallback()
  },
}
