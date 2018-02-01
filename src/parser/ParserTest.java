package parser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ParserTest {

	@Test
	void testFactor() {
		Parser parser = new Parser("43", false);
		parser.factor();
	}

}
