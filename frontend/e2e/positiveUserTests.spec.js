import { test, expect } from '@playwright/test';

const testUserData = {
  matrikelNr: '5011657',
  firstName: 'Test',
  lastName: 'User',
  email: 'test00001@htwsaar.de',
  password: 'Password'
}

// See here how to get started:
// https://playwright.dev/docs/intro
test('visit the root page and go to sign up page', async ({ page }) => {
  await page.goto('/');
  await expect(page.locator('h1')).toHaveText('HASE');

  await expect(page.locator('.signup-text > a:nth-child(1)')).toHaveText('Sign up here!');
  await page.click('.signup-text > a:nth-child(1)')
  await expect(page.locator('.container > p:nth-child(3)')).toHaveText('Please enter your registration data!');
  await expect(page.locator('.input-group > button:nth-child(7)')).toHaveText('Register')
})

test('visit sign up page and sign up with a non existant account and login afterwards', async ({page}) => {
  await page.goto('/register');
  await expect(page.locator('.container > p:nth-child(3)')).toHaveText('Please enter your registration data!');
  await expect(page.locator('.input-group > button:nth-child(7)')).toHaveText('Register')

  await page.fill('.input-group > input:nth-child(1)', testUserData.matrikelNr)
  await page.fill('.input-group > input:nth-child(2)', testUserData.firstName)
  await page.fill('.input-group > input:nth-child(3)', testUserData.lastName)
  await page.fill('.input-group > input:nth-child(4)', testUserData.email)
  await page.fill('.input-group > input:nth-child(5)', testUserData.password)

  await page.click('.input-group > button:nth-child(7)')
  await page.waitForSelector('#success-text')

  await page.click('.container > div:nth-child(1) > p:nth-child(2) > a:nth-child(1)')
  await expect(page.locator('.container > p:nth-child(3)')).toHaveText('Please enter your login data')

  await page.fill('.input-group > input:nth-child(1)', testUserData.email)
  await page.fill('.input-group > input:nth-child(2)', testUserData.password)
  await page.click('.container > button:nth-child(6)')

  await page.waitForURL('http://localhost:5173/my-appointments')
  expect(page.url()).toBe('http://localhost:5173/my-appointments')
})


test('log in and create a test appointment in the common appointment pool', async ({ page }) => {
  await page.goto('/login')
  await page.fill('.input-group > input:nth-child(1)', testUserData.email)
  await page.fill('.input-group > input:nth-child(2)', testUserData.password)
  await page.click('.container > button:nth-child(6)')

  await page.waitForURL('http://localhost:5173/my-appointments')
  expect(page.url()).toBe('http://localhost:5173/my-appointments')

  await page.click('.top-container > nav:nth-child(1) > a:nth-child(3)')
  await expect(page.locator('.add-button')).toHaveText('➕ Make a new Appointment!')

  await page.click('.add-button')
  await page.fill('input.popup-input:nth-child(2)', 'Test Appointment')
  await page.fill('input.popup-input:nth-child(4)', 'Test Location')
  await page.click('.add-btn')

  await page.waitForSelector('.appointment-card')
  await page.click('.edit-btn')
  await page.fill('.appointment-content > div:nth-child(1) > input:nth-child(1)', 'New Appointment')
  await page.click('.confirm-btn')

  await page.click('.delete-btn')
})

test('log in, create test appointment in the common appointment pool, edit the appointment, book it and check in the my appointments section', async ({ page }) => {
  await page.goto('/login')
  await page.fill('.input-group > input:nth-child(1)', testUserData.email)
  await page.fill('.input-group > input:nth-child(2)', testUserData.password)
  await page.click('.container > button:nth-child(6)')

  await page.waitForURL('http://localhost:5173/my-appointments')
  expect(page.url()).toBe('http://localhost:5173/my-appointments')

  await page.click('.top-container > nav:nth-child(1) > a:nth-child(3)')
  await expect(page.locator('.add-button')).toHaveText('➕ Make a new Appointment!')

  await page.click('.add-button')
  await page.fill('input.popup-input:nth-child(2)', 'Test Appointment')
  await page.fill('input.popup-input:nth-child(4)', 'Test Location')
  await page.click('.add-btn')

  await page.waitForSelector('.appointment-card')
  await page.click('.edit-btn')
  await page.fill('.appointment-content > div:nth-child(1) > input:nth-child(1)', 'New Appointment')
  await page.click('.confirm-btn')
  await expect(page.locator('div.appointment-card:nth-child(1) > div:nth-child(2) > div:nth-child(1)')).toHaveText('📌 New Appointment')

  await page.click('.button-group > button:nth-child(3)')
  await page.click('#app > header:nth-child(1) > div:nth-child(1) > nav:nth-child(1) > a:nth-child(1)')
  await expect(page.locator('div.appointment-card:nth-child(1) > div:nth-child(2) > div:nth-child(1)')).toHaveText('📌 New Appointment')
})
