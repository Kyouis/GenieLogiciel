package test;

import org.junit.jupiter.api.Test;
import static  org.assertj.core.api.Assertions.assertThat;


public class TestApp {

    @Test
    void testTest() {
        assertThat("test0").isSameAs("test0");
    }
}
