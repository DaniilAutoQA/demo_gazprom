package cloud.autotests.tests;

import cloud.autotests.helpers.DriverUtils;
import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;


public class GazpromBankTests extends TestBase {
    @Test
    @Description("Тест проверяет, что открыта главная страница банк по title страницы")
    @DisplayName("Verify title mane page")
    void titleTest() {
        step("Открыть главную страницу Газпром банка'", () ->
                open("https://www.gazprombank.ru/"));
        step("Проверить содержание тескста 'Газпромбанк — «Газпромбанк» (Акционерное общество)'", () -> {
            String expectedTitle = "Газпромбанк — «Газпромбанк» (Акционерное общество)";
            String actualTitle = title();
            assertThat(actualTitle).isEqualTo(expectedTitle);
        });
    }

    @Test
    @Description("Тест выполняет проверку на надиие состов секции 'Выберите категорию'")
    @DisplayName("Verification section choice of categories")
    void verifyTitleOfCategory() {
        step("Открыть главную страницу Газпром банка", () -> {
            open("https://www.gazprombank.ru/");
        });
        step("Проверить секцию выбора категорий", () -> {
            step("Проверить отображение и название секции 'Выберите категорию'", () -> {
                $(".nr-categories__title").should(visible);
                $(".nr-categories__title").shouldHave(text("Выберите категорию"));
            });
            step("Проверить наименование доступных категорий", () -> {
                $$(".nr-categories-tabs__el").shouldHave(size(6), texts(asList("Популярное", "Накопления", "Ипотека", "Автолюбителям", "Акции", "Устойчивое развитие")));
            });
        });
    }

    @Test
    @Description("Тест выполняет проверку расчета дохода по вкладу «Ваш успех» без учета капитализации")
    @DisplayName("Verification calculation deposit")
    void verifyDeposits() {
        step("Открыть главную страницу Газпром банка", () -> {
            open("https://www.gazprombank.ru/");
        });
        step("Выбрать категорию Накопления", () -> {
            $$(".nr-categories-tabs__tab").find(text("Накопления")).scrollTo().click();
        });
        step("Выбрать вклад 'Ваш Успех'", () -> {
            $("[href='/personal/increase/deposits/detail/6049/']").click();
        });
        step("Перейти к рассчету дохода", () -> {
            $$(".nn-btn__text ").find(text("Рассчитать доход")).click();
        });
        step("Ввести данные для рассчета", () -> {
            $(".nn-range__value").setValue("9000000");
        });
        step("Снять чекбокс 'Капитализация'", () -> {
            $(".nn-checkbox").click();
        });
        step("Проверить рассчет", () -> {
            step("Сумма в конце срока должна быть  9 494 092", () ->{
                $(".js-nn-calc-rSum").shouldHave(text("9 494 092"));
            });
            step("Доход должен быть 494 092", () ->{
                $(".js-nn-calc-rProfit").shouldHave(text("494 092"));
            });
            step("Процентная ставка должна быть 5.46%", () ->{
                $(".js-nn-calc-rRate").shouldHave(text("5.46"));
            });
            step("Эффективная ставка не должна отображаться", () ->{
                $(".js-nn-calc-effective").shouldNot(visible);
            });
        });
    }

    @Test
    @Description("Тест проверяет наличие ошибок в консоле, которые содержат текст «Ваш успех»")
    @DisplayName("Page console log should not have errors")
    void consoleShouldNotHaveErrorsTest() {
        step("Open url 'https://www.gazprombank.ru/'", () ->
                open("https://www.gazprombank.ru/"));

        step("Console logs should not contain text 'SEVERE'", () -> {
            String consoleLogs = DriverUtils.getConsoleLogs();
            String errorText = "SEVERE";

            assertThat(consoleLogs).doesNotContain(errorText);
        });
    }
}