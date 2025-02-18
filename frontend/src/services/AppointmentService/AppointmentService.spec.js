import {test, expect} from 'vitest'
import AppointmentService from '@/services/AppointmentService/AppointmentService.js'

test('post appointment with nothing entered should throw error', async () => {
  await expect((async () => {
    await AppointmentService.addAppointment()
  })()).rejects.toThrowError('Please fill in all the required fields!')
})

test('post appointment with not fully filled in details', async () => {
  await expect((async () => {
    const testAppointment = {
      name: 'Test appointment',
      date: new Date(),
      location: ''
    }

    await AppointmentService.addAppointment(testAppointment)
  })()).rejects.toThrowError('location is required')
})

test('delete appointment with nothing entered should throw error', async () => {
  await expect((async () => {
    await AppointmentService.deleteAppointment()
  })()).rejects.toThrowError('Please fill in all the required fields!')
})

test('delete appointment with empty appointmentId should throw error', async () => {
  await expect((async () => {
    const testAppointment = {
      appointmentId: '',
      name: 'Test appointment',
      date: new Date(),
      location: 'Test location',
    }

    await AppointmentService.deleteAppointment(testAppointment)
  })()).rejects.toThrowError('appointmentId is required')
})

test('update appointment with nothing entered should throw error', async () => {
  await expect((async () => {
    await AppointmentService.updateAppointment()
  })()).rejects.toThrowError('Please fill in all the required fields!')
})

test('update appointment with empty appointmentId should throw error', async () => {
  await expect((async () => {
    const testAppointment = {
      appointmentId: '',
      name: 'Test appointment',
      date: new Date(),
      location: 'Test location',
    }

    await AppointmentService.updateAppointment(testAppointment)
  })()).rejects.toThrowError('appointmentId is required')
})
