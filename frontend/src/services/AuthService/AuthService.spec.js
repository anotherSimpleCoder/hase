import {test, expect} from 'vitest'
import LoginService from '@/services/AuthService/AuthService.js'

test('login with nothing entered should throw error', async() => {
  await expect((async () => {
    await LoginService.login()
  })()).rejects.toThrowError('Please fill in all the required fields!')
})

test('login with not fully filled in details should throw error', async() => {
  const testLogin = {
    email: '',
    password: 'huh',
  }

  await expect((async () => {
    await LoginService.login(testLogin)
  })()).rejects.toThrowError('email is required')
})

test('get user by entering no mail should throw error', async() => {
  await expect((async () => {
    await LoginService.getMe()
  })()).rejects.toThrowError('Invalid request: token is missing!')
})
