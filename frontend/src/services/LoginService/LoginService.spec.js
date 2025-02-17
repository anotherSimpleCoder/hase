import {test, expect} from 'vitest'
import LoginService from '@/services/LoginService/LoginService.js'

test('login with no nothing entered should throw error', async() => {
  await expect((async () => {
    await LoginService.login()
  })()).rejects.toThrowError('Please fill in all the required fields!')
})

test('login with not fully filled in details', async() => {
  const testLogin = {
    email: '',
    password: 'huh',
  }

  await expect((async () => {
    await LoginService.login(testLogin)
  })()).rejects.toThrowError('email is required')
})
