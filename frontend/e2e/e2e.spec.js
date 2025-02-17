import { expect, test } from '@playwright/test'

test('visit the root page and have a valid landing page', async ({ page }) => {
  await page.goto('/');
  await expect(page.locator('h1')).toHaveText('HASE');
  await expect(page.locator('.signup-text > a:nth-child(1)')).toHaveText('Sign up here!');
})
