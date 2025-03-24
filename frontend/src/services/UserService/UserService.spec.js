import { expect, test } from 'vitest'
import UserService from '@/services/UserService/UserService.js'

test('register empty user should throw error', async () => {
  await expect((async () => {
    await UserService.register()
  })()).rejects.toThrowError("Please fill in all the required fields!")
})

test('register user with not all details being filled out', async () => {
  const testUser = {
    matrikelNr: '5011657',
    firstName: 'test',
    lastName: 'user',
    email: '',
    password: 'testPassword',
  }

  await expect((async () => {
    await UserService.register(testUser)
  })()).rejects.toThrowError('email is required')
})

test('get user with invalid matrikel number, should throw error', async () => {
  await expect((async () => {
    await UserService.getUserByMatrikelNr()
  })).rejects.toThrowError('MatrikelNr is required')
})
