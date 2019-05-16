package pl.demo;

import pl.demo.controller.BookControllerTest;
import pl.demo.controller.HomeControllerTest;
import pl.demo.rest.BookControllerMockTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        HomeControllerTest.class,
        BookControllerTest.class,
        BookControllerMockTest.class
})
public class AllTestsSuite {

}
