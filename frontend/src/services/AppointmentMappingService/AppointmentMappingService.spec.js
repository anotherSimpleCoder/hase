import {test, expect} from 'vitest'
import AppointmentMappingService from './AppointmentMappingService'

test('post invalid mapping should throw error', async () => {
  await expect((async () => {
    await AppointmentMappingService.postAppointmentforUser()
  })()).rejects.toThrowError('Mapping is required')
})

test('get appointment for empty Matrikelnummer should throw error', async () => {
  await expect((async () => {
    await AppointmentMappingService.getAppointmentsForUser()
  })()).rejects.toThrowError('User is required')
})

test('remove invalid mapping should throw error', async () => {
  await expect((async () => {
    await AppointmentMappingService.postAppointmentforUser()
  })()).rejects.toThrowError('Mapping is required')
})
