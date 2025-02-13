import { test, expect } from '@playwright/test';

// See here how to get started:
// https://playwright.dev/docs/intro
test('visit the root page and go to sign up page', async ({ page }) => {
  await page.goto('/');
  await expect(page.locator('.container > h1:nth-child(1)')).toHaveText('HASE');

  await page.click('.signup-text > a:nth-child(1)')
  await expect(page.locator('.container > p:nth-child(2)')).toHaveText('Please enter your registration data!');
  await expect(page.locator('.input-group > button:nth-child(7)')).toHaveText('Register')
})

test('visit sign up page and sign up with a non existant account and login afterwards', async ({page}) => {
  const testUserData = {
    matrikelNr: '5011657',
    firstName: 'Test',
    lastName: 'User',
    email: 'test00001@htwsaar.de',
    password: 'Password'
  }

  await page.goto('/register');
  await expect(page.locator('.container > p:nth-child(2)')).toHaveText('Please enter your registration data!');
  await expect(page.locator('.input-group > button:nth-child(7)')).toHaveText('Register')

  await page.fill('.input-group > input:nth-child(1)', testUserData.matrikelNr)
  await page.fill('.input-group > input:nth-child(2)', testUserData.firstName)
  await page.fill('.input-group > input:nth-child(3)', testUserData.lastName)
  await page.fill('.input-group > input:nth-child(4)', testUserData.email)
  await page.fill('.input-group > input:nth-child(5)', testUserData.password)

  await page.click('.input-group > button:nth-child(7)')
  await page.waitForSelector('#success-text')

  await page.click('.container > div:nth-child(1) > p:nth-child(2) > a:nth-child(1)')
  await expect(page.locator('.container > p:nth-child(2)')).toHaveText('Please enter your login data')

  await page.fill('.input-group > input:nth-child(1)', testUserData.email)
  await page.fill('.input-group > input:nth-child(2)', testUserData.password)
  await page.click('.container > button:nth-child(5)')

  await page.waitForURL('http://localhost:5173/my-appointments')
  expect(page.url()).toBe('http://localhost:5173/my-appointments')
})
