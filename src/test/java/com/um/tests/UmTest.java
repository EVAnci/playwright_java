package com.um.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import org.junit.jupiter.api.Test;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UmTest {
  @Test
  void comprobarLibro() {
    
    try (Playwright playwright = Playwright.create()) {
      Browser browser = playwright.firefox().launch(
            new BrowserType.LaunchOptions().setHeadless(false)
          );

      Page page = browser.newPage();

      // 1. Ir a la página de la universidad
      page.navigate("https://www.um.edu.ar/");
  
      // 2. Hacer hover sobre Estudiantes
      page.getByRole(
          AriaRole.LINK,
          new Page.GetByRoleOptions().setName("Estudiantes").setExact(true)
          ).hover();

      // 3. Hacer clic en Biblioteca
      page.getByRole(
          AriaRole.LINK,
          new Page.GetByRoleOptions().setName("Biblioteca").setExact(true)
          ).click();

      // 4. Hacer clic en Consultá nuestro catálogo 
      page.getByRole(
          AriaRole.LINK,
          new Page.GetByRoleOptions().setName("Consultá nuestro catálogo").setExact(true)
          ).click();

      page.waitForTimeout(2000);
      // 5. Introducir el título del libro 
      Locator buscador = page.locator("#translControl1");
      buscador.pressSequentially(
          "UML y Patrones",
          new Locator.PressSequentiallyOptions().setDelay(100)
          );

      page.waitForTimeout(2000);
      // 6. Seleccionar la sede de mendoza
      Locator sede = page.locator("#select_library");
      sede.selectOption("branch:01");

      // 7. Compruebo selecccion de sede y texto
      assertThat(buscador).hasValue("UML y Patrones");
      assertThat(sede).hasValue("branch:01");

      // 7.1 Esperar un pequeño tiempo
      page.waitForTimeout(2000);

      // 8. Hacer clic en Buscar 
      page.locator("#searchsubmit").click();
      // page.waitForTimeout(6000);
      // page.goBack();
      // page.goBack();

      // page.waitForTimeout(2000);
      // page.locator("#searchsubmit").click();
      // page.waitForTimeout(6000);
      // page.goBack();
      // page.goBack();

      // page.waitForTimeout(2000);
      // page.locator("#searchsubmit").click();
      // page.waitForTimeout(6000);
      // No se si esta triquiñuela es válida. Lo correcto sería pedir
      // un entorno de prueba sin la validación captcha. Para resolverlo
      // probando encontré que tras 3 intentos se aprueba el captcha.

      // 9. Comprobar que el libro existe
      assertThat(
          page.locator(".record h1.title")
          ).hasText("UML Y PATRONES");
      assertThat(
          page.locator("[property='isbn']")
          ).hasText("8420534382");

      browser.close();
    }
  }
}
