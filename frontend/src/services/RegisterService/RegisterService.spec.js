import {test, expect} from 'vitest'
import RegisterService from '@/services/RegisterService/RegisterService.js'

test('register empty user should throw error', async () => {
  await expect((async () => {
    await RegisterService.register()
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
    await RegisterService.register(testUser)
  })()).rejects.toThrowError('email is required')
})
