package io.example.jwt.config;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

@Disabled
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.MethodName.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class BaseTestAnnotations {
}
