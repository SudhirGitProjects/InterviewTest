package com.UI;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class LeadManager_UI_Test {

    @Test
    public void validateAdminUserFlow() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://v0-lead-manager-app.vercel.app/login");
        driver.findElement(By.id("email")).sendKeys("admin@company.com");
        driver.findElement(By.id("password")).sendKeys("Admin@123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        WebElement role = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-testid='leads-user-role-badge']")
                )
        );
        String userRole = role.getText();
        Assert.assertEquals(userRole, "Administrator");
        driver.findElement(By.cssSelector("button[data-testid='leads-create-new-btn']")).click();
        driver.findElement(By.cssSelector("#create-name")).sendKeys("Sudhir Kumar Rakesh");
        driver.findElement(By.cssSelector("#create-email")).sendKeys("sudhirkumar21212@gmail.com");
        driver.findElement(By.cssSelector("#create-phone")).sendKeys("+911121222222");
        driver.findElement(By.cssSelector("#create-company")).sendKeys("osmos ai tech");
        driver.findElement(By.cssSelector("#create-job-title")).sendKeys("QA Lead");

        selectCustomDropdownOption(driver, wait, "create-form-industry-select", "Education");
        selectCustomDropdownOption(driver, wait, "create-form-source-select", "Trade Show");
        selectCustomDropdownOption(driver, wait, "create-form-priority-select", "High");
        selectCustomDropdownOption(driver, wait, "create-form-status-select", "Qualified");

        driver.findElement(By.cssSelector("#create-deal-value")).sendKeys("5000");
        driver.findElement(By.cssSelector("#create-follow-up")).sendKeys("22-08-2027");
        driver.findElement(By.cssSelector("#create-expected-close")).sendKeys("22-02-2029");
        driver.findElement(By.cssSelector("#create-is-qualified")).click();
        //driver.findElement(By.cssSelector("#create-form-submit-btn")).click();
        driver.findElement(By.cssSelector("button[data-testid='create-form-submit-btn']")).click();

        WebElement messageElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("div.grid.gap-1 > div:nth-child(2)")
                )
        );

        String message = messageElement.getText();
        System.out.println("Success Message: " + message);
        Assert.assertEquals("Lead created successfully", message);

        driver.findElement(By.cssSelector("[data-testid='leads-search-input']")).sendKeys("Sudhir");

        WebElement editLead = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[title='Edit lead']")));
        editLead.click();


        driver.findElement(By.cssSelector("#edit-email")).clear();
        driver.findElement(By.cssSelector("#edit-email")).sendKeys("sudhir2121@gmail.com");
        driver.findElement(By.cssSelector("[data-testid= 'edit-form-submit-btn']")).click();

        WebElement deleteLead = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[data-testid='lead-delete-btn-21']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteLead);
        Alert alert = driver.switchTo().alert();
        alert.accept();


    }

    public void selectCustomDropdownOption(WebDriver driver, WebDriverWait wait, String dropdownTestId, String optionText) {

        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[data-testid='" + dropdownTestId + "']")
        ));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", dropdown);

        dropdown.click();

        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='option' and normalize-space()='" + optionText + "']")
        ));

        option.click();
    }
}
