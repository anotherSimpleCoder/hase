import { test, expect } from '@playwright/test';

test('visit login page', async ({page}) => {
  await page.goto('/login')
  await expect(page.locator('.container > p:nth-child(2)')).toHaveText('Please enter your login data')
  await expect(page.locator('.input-group > input:nth-child(1)')).toBeVisible()
  await expect(page.locator('.input-group > input:nth-child(2)')).toBeVisible()
})

test('visit the login page and login with only email', async({page}) => {
  await page.goto('/login')
  await expect(page.locator('.container > p:nth-child(2)')).toHaveText('Please enter your login data')
  await expect(page.locator('.input-group > input:nth-child(1)')).toBeVisible()
  await expect(page.locator('.input-group > input:nth-child(2)')).toBeVisible()

  await page.fill('.input-group > input:nth-child(1)', 'invalid@email.com')
  await page.click('.container > button:nth-child(5)')

  await page.waitForSelector('#error-text')
  await expect(page.locator('#error-text')).toHaveText(`Error: password is required`)
})

test('visit the login page and login with only password', async({page}) => {
  await page.goto('/login')
  await expect(page.locator('.container > p:nth-child(2)')).toHaveText('Please enter your login data')
  await expect(page.locator('.input-group > input:nth-child(1)')).toBeVisible()
  await expect(page.locator('.input-group > input:nth-child(2)')).toBeVisible()

  await page.fill('.input-group > input:nth-child(2)', 'invalidPassword')
  await page.click('.container > button:nth-child(5)')

  await expect(page.locator('#error-text')).toHaveText(`Error: email is required`)
})

test('visit the login page and login with non existant user', async ({page}) => {
  await page.goto('/login')
  await expect(page.locator('.container > p:nth-child(2)')).toHaveText('Please enter your login data')
  await expect(page.locator('.input-group > input:nth-child(1)')).toBeVisible()
  await expect(page.locator('.input-group > input:nth-child(2)')).toBeVisible()

  await page.fill('.input-group > input:nth-child(1)', 'invalid@email.com')
  await page.fill('.input-group > input:nth-child(2)', 'invalidPassword')
  await page.click('.container > button:nth-child(5)')

  await expect(page.locator('#error-text')).toHaveText(`Error: invalid@email.com is not found`)
})
