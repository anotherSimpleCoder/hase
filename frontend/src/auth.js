import { UserManager } from 'oidc-client-ts'

const config = {
  authority: 'https://accounts.google.com',
  client_id: '371949021462-ln0r7kjjjdbtofl10ehnufjo7akt99d4.apps.googleusercontent.com',
  redirect_uri: 'http://localhost:5173/callback',
  response_type: 'code',
  scope: 'openid profile email',
  client_secret: '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',
}

const userManager = new UserManager(config)

export default {
  login() {
    return userManager.signinRedirect()
  },
  logout() {
    return userManager.signoutRedirect()
  },
  getUser() {
    return userManager.getUser()
  },
  handleCallback() {
    return userManager.signinRedirectCallback()
  },
}
