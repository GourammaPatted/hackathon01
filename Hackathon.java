package org.hackaton;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Hackathon {
    private AndroidDriver driver;

    @Test
    public void LongPress() throws MalformedURLException, URISyntaxException, InterruptedException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Pixel_9");
        options.setPlatformName("Android");
        options.setApp("C:\\Users\\user\\Downloads\\apk\\ApiDemos-debug.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        Thread.sleep(4000);

        driver.findElement(AppiumBy.accessibilityId("Views")).click();
        Thread.sleep(2000);
        driver.findElement(AppiumBy.accessibilityId("Expandable Lists")).click();
        Thread.sleep(2000);
        driver.findElement(AppiumBy.xpath("//android.widget.TextView[@content-desc='1. Custom Adapter']")).click();
        Thread.sleep(2000);
        WebElement catNames = driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Cat Names']"));

        // Long Press on Cat Names
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), catNames.getLocation()))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofSeconds(2)))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(sequence));

        Thread.sleep(2000);
        driver.findElement(AppiumBy.xpath("//android.widget.TextView[@text='Sample action']")).click();
        Thread.sleep(2000);

        for (int i = 0; i < 3; i++) {
            driver.navigate().back();
        }
        Thread.sleep(2000);
    }

    @Test
    public void ScrollTest() throws MalformedURLException, URISyntaxException, InterruptedException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Pixel_9");
        options.setPlatformName("Android");
        options.setApp("C:\\Users\\user\\Downloads\\apk\\ApiDemos-debug.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        Thread.sleep(4000);

        driver.findElement(AppiumBy.accessibilityId("Views")).click();
        Thread.sleep(2000);

        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().text(\"WebView3\"))"));
        Thread.sleep(2000);
        System.out.println("WebView3 is now visible.");

        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().text(\"Animation\"))"));
        Thread.sleep(2000);
        System.out.println("Animation is now visible.");

        driver.findElement(AppiumBy.accessibilityId("Animation")).click();
        Thread.sleep(2000);
        System.out.println("Animation clicked!");
    }


    @Test
    public void swipeThroughGalleryPhotos() throws MalformedURLException, URISyntaxException, InterruptedException {
        // Set up Appium options
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Pixel_9");
        options.setPlatformName("Android");
        options.setApp("C:\\Users\\user\\Downloads\\apk\\ApiDemos-debug.apk");

        // Create driver instance
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        Thread.sleep(4000);

        // Navigate to Views > Gallery > Photos
        driver.findElement(AppiumBy.accessibilityId("Views")).click();
        Thread.sleep(2000);
        driver.findElement(AppiumBy.accessibilityId("Gallery")).click();
        Thread.sleep(2000);
        driver.findElement(AppiumBy.accessibilityId("1. Photos")).click();
        Thread.sleep(2000);


        List<WebElement> images = driver.findElements(AppiumBy.className("android.widget.ImageView"));

        try {
            // Get Y-axis position of images
            WebElement firstImage = images.get(0);
            int centerY = firstImage.getLocation().getY() + (firstImage.getSize().getHeight() / 2);
            int startX = firstImage.getLocation().getX() + firstImage.getSize().getWidth() - 10;
            int endX = firstImage.getLocation().getX() + 10;

            // Swipe right-to-left 4 times to reach the 5th image
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            for (int i = 0; i < 4; i++) {
                Sequence swipe = new Sequence(finger, 1)
                        .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, centerY)) // Start at right edge
                        .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                        .addAction(new Pause(finger, Duration.ofMillis(300)))
                        .addAction(finger.createPointerMove(Duration.ofMillis(700), PointerInput.Origin.viewport(), endX, centerY)) // Swipe to left
                        .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

                driver.perform(Collections.singletonList(swipe));
                Thread.sleep(1500);
            }

            images = driver.findElements(AppiumBy.className("android.widget.ImageView"));

            // Verify 5th image is displayed
            if (images.get(4).isDisplayed()) {
                System.out.println(" 5th image is displayed!");
            } else {
                throw new RuntimeException(" 5th image is NOT visible after swiping!");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

        driver.quit();
    }

    @Test
    public void performDragAndDrop() throws MalformedURLException, URISyntaxException, InterruptedException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Pixel_9");
        options.setPlatformName("Android");
        options.setApp("C:\\Users\\user\\Downloads\\apk\\ApiDemos-debug.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        Thread.sleep(4000);

        driver.findElement(AppiumBy.accessibilityId("Views")).click();
        Thread.sleep(2000);
        driver.findElement(AppiumBy.accessibilityId("Drag and Drop")).click();
        Thread.sleep(2000);

        // Find the source and target dots
        WebElement sourceDot = driver.findElement(AppiumBy.id("io.appium.android.apis:id/drag_dot_1"));
        WebElement targetDot = driver.findElement(AppiumBy.id("io.appium.android.apis:id/drag_dot_2"));

        // Get source and target coordinates
        int startX = sourceDot.getLocation().getX() + (sourceDot.getSize().getWidth() / 2);
        int startY = sourceDot.getLocation().getY() + (sourceDot.getSize().getHeight() / 2);
        int endX = targetDot.getLocation().getX() + (targetDot.getSize().getWidth() / 2);
        int endY = targetDot.getLocation().getY() + (targetDot.getSize().getHeight() / 2);

        // Perform Drag and Drop using PointerInput
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence dragAndDrop = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY)) // Move to the source dot
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg())) // Press down (Long Press)
                .addAction(new org.openqa.selenium.interactions.Pause(finger, Duration.ofSeconds(2))) // Hold for 2 seconds
                .addAction(finger.createPointerMove(Duration.ofMillis(700), PointerInput.Origin.viewport(), endX, endY)) // Move to target dot
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg())); // Release

        driver.perform(Collections.singletonList(dragAndDrop));

        System.out.println(" Drag and Drop performed successfully!");
        driver.quit();
    }

    @Test
    public void tapOnFlip() throws MalformedURLException, URISyntaxException, InterruptedException {
        // Set up Appium options
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Pixel_9");
        options.setPlatformName("Android");
        options.setApp("C:\\Users\\user\\Downloads\\apk\\ApiDemos-debug.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);

        try {
            Thread.sleep(4000);

            // Navigate to Animation > View Flip
            driver.findElement(AppiumBy.accessibilityId("Animation")).click();
            Thread.sleep(2000);
            driver.findElement(AppiumBy.accessibilityId("View Flip")).click();
            Thread.sleep(2000);

            // Locate Flip button
            WebElement flipButton = driver.findElement(AppiumBy.accessibilityId("Flip"));

            // Perform Tap using JavaScriptExecutor (clickGesture)
            tap(flipButton);

            Thread.sleep(2000);
            System.out.println("✅ Flip button tapped successfully!");

        } finally {
            // Close driver properly
            driver.quit();
        }
    }


    @Test
    public void handleSimpleAlert() throws MalformedURLException, URISyntaxException, InterruptedException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Pixel_9");
        options.setPlatformName("Android");
        options.setApp("C:\\Users\\user\\Downloads\\apk\\ApiDemos-debug.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        Thread.sleep(4000);

        driver.findElement(AppiumBy.accessibilityId("App")).click();
        Thread.sleep(2000);
        driver.findElement(AppiumBy.accessibilityId("Alert Dialogs")).click();
        Thread.sleep(2000);

        // Click on the first alert option
        WebElement firstAlertButton = driver.findElement(AppiumBy.accessibilityId("OK Cancel dialog with a message"));
        firstAlertButton.click();
        Thread.sleep(2000);

        // Capture and extract the alert text
        Alert alert = driver.switchTo().alert();
        String alertText = alert.getText();
        System.out.println(" Alert Text: " + alertText);

        // Accept and close the alert
        alert.accept();
        System.out.println(" Alert accepted!");
    }

    @Test
    public void handleSingleChoiceAlert() throws MalformedURLException, URISyntaxException, InterruptedException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Pixel_9");
        options.setPlatformName("Android");
        options.setApp("C:\\Users\\user\\Downloads\\apk\\ApiDemos-debug.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        Thread.sleep(4000);

        driver.findElement(AppiumBy.accessibilityId("App")).click();
        Thread.sleep(2000);
        driver.findElement(AppiumBy.accessibilityId("Alert Dialogs")).click();
        Thread.sleep(2000);

        // Click on "Single Choice List"
        WebElement singleChoiceButton = driver.findElement(AppiumBy.accessibilityId("Single choice list"));
        singleChoiceButton.click();
        Thread.sleep(3000);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        var streetViewOption = wait.until(ExpectedConditions.presenceOfElementLocated(
                AppiumBy.xpath("//android.widget.CheckedTextView[@text='Street view']")));
        streetViewOption.click();
        System.out.println("Selected 'Street view' radio button");

        // Confirm and save the selection by clicking "OK"
        WebElement okButton = driver.findElement(AppiumBy.id("android:id/button1"));
        okButton.click();
        System.out.println("Selection confirmed and saved!");

        driver.quit();
    }

    @Test
    public void handleTextEntryDialog() throws MalformedURLException, URISyntaxException, InterruptedException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Pixel_9");
        options.setPlatformName("Android");
        options.setApp("C:\\Users\\user\\Downloads\\apk\\ApiDemos-debug.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        Thread.sleep(4000);

        driver.findElement(AppiumBy.accessibilityId("App")).click();
        driver.findElement(AppiumBy.accessibilityId("Alert Dialogs")).click();

        driver.findElement(AppiumBy.accessibilityId("Text Entry dialog")).click();

        // Wait for the dialog to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(AppiumBy.className("android.widget.EditText")));

        // Enter text in input fields
        List<WebElement> inputFields = driver.findElements(AppiumBy.className("android.widget.EditText"));
        inputFields.get(0).sendKeys("JohnDoe");
        inputFields.get(1).sendKeys("SecurePass123");

        // Click OK to save details
        driver.findElement(AppiumBy.id("android:id/button1")).click();
        System.out.println(" Name & Password entered and saved!");
    }


    @Test
    public void handleRepeatAlarm() throws MalformedURLException, URISyntaxException, InterruptedException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("Pixel_9");
        options.setPlatformName("Android");
        options.setApp("C:\\Users\\user\\Downloads\\apk\\ApiDemos-debug.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
        Thread.sleep(4000);

        driver.findElement(AppiumBy.accessibilityId("App")).click();
        driver.findElement(AppiumBy.accessibilityId("Alert Dialogs")).click();

        driver.findElement(AppiumBy.accessibilityId("Repeat alarm")).click();

        List<String> daysToUncheck = Arrays.asList("Every Tuesday", "Every Thursday");
        for (String day : daysToUncheck) {
            WebElement checkbox = driver.findElement(AppiumBy.xpath("//android.widget.CheckedTextView[@text='" + day + "']"));
            if (checkbox.getAttribute("checked").equals("true")) {
                checkbox.click();
            }
        }

        // Check Monday to Friday
        List<String> daysToCheck = Arrays.asList("Every Monday", "Every Tuesday", "Every Wednesday", "Every Thursday", "Every Friday");
        for (String day : daysToCheck) {
            WebElement checkbox = driver.findElement(AppiumBy.xpath("//android.widget.CheckedTextView[@text='" + day + "']"));
            if (checkbox.getAttribute("checked").equals("false")) {
                checkbox.click();
            }
        }

        // Click OK to save changes
        driver.findElement(AppiumBy.id("android:id/button1")).click();
        System.out.println("✅ Alarm updated: Mon-Fri checked, Tuesday-Thursday unchecked!");
    }

    public void tap(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("mobile: clickGesture", ImmutableMap.of(
                "elementId", ((RemoteWebElement) element).getId()
        ));
    }
}

