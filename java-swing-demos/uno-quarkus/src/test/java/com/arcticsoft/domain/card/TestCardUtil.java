package com.arcticsoft.domain.card;

import com.arcticsoft.domain.testhelper.CardTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestCardUtil {

    @Test
    void GivenWildColorCard_ShouldBeWildCard() {
        var result = CardUtil.isWildCard(CardTestFactory.createWildColorCard());
        Assertions.assertTrue(result);
    }

    @Test
    void GivenWildDrawFourCard_ShouldBeWildCard() {
        var result = CardUtil.isWildCard(CardTestFactory.createWildDrawFourCard());
        Assertions.assertTrue(result);
    }

    @Test
    void GivenNumberCard_ShouldNotBeWildCard() {
        var result = CardUtil.isWildCard(CardTestFactory.createNumberCard());
        Assertions.assertFalse(result);
    }
}
