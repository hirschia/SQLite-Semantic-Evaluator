package symbolicexecution.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import symbolicexecution.SymbolicExecution;
import symbolicexecution.Fact;
import symbolicexecution.exceptions.ReferenceNotFoundException;
import symbolicexecution.exceptions.SymbolNotRecognizedException;

class SymbolicExecutionTest {
	
	private SymbolicExecution se;
	
	@BeforeEach
	void setUp() {
		se = new SymbolicExecution();
	}
	
	@AfterEach
	void tearDown() {
		se.close();
	}
	
	@Test
	@DisplayName("Exception tests")
	void testExceptions() {
		assertThrows(ReferenceNotFoundException.class, () -> se.binaryOperation("", "", "="));
		assertThrows(ReferenceNotFoundException.class, () -> se.binaryOperation(null, null, "="));
		assertThrows(ReferenceNotFoundException.class, () -> se.binaryOperation("a", "b", "="));
		String ref = se.nummericLiteral("1").getIdentifier();
		assertThrows(SymbolNotRecognizedException.class, () -> se.binaryOperation(ref, ref, "some random string"));
	}
	
	@Test
	@DisplayName("Integer literal tests")
	void testIntegerLiterals() {
		int isZero = SymbolicExecution.STATUS_ZERO, 
			isNotZero = SymbolicExecution.STATUS_NONZERO,
			isOne = SymbolicExecution.STATUS_NONZERO | SymbolicExecution.STATUS_ONE;
		assertAll("Status codes", 
			() -> assertEquals(isZero, se.nummericLiteral(0).getStatusCode()),
			() -> assertEquals(isZero, se.nummericLiteral("0").getStatusCode()),
			() -> assertEquals(isOne, se.nummericLiteral(1).getStatusCode()),
			() -> assertEquals(isNotZero, se.nummericLiteral(-1).getStatusCode()),
			() -> assertEquals(isNotZero, se.nummericLiteral(5000).getStatusCode()),
			() -> assertEquals(isOne, se.nummericLiteral("1").getStatusCode()),
			() -> assertEquals(isNotZero, se.nummericLiteral("-1").getStatusCode()),
			() -> assertEquals(isNotZero, se.nummericLiteral("5000").getStatusCode()),
			() -> assertEquals(isZero, se.nummericLiteral("-0").getStatusCode())
		);
	}
	
	@Test
	@DisplayName("Binary operation with literal integers")
	void testIntegerLiteralBinOP() {
		String lit_0 = se.nummericLiteral("0").getIdentifier(),
			lit_1 = se.nummericLiteral("1").getIdentifier(),
			lit_5 = se.nummericLiteral("5").getIdentifier(),
			lit_minus_1 = se.nummericLiteral("-1").getIdentifier();
		int isTaut = SymbolicExecution.STATUS_TAUTOLOGY,
			isUnsat = SymbolicExecution.STATUS_UNSATISFIABLE;
		
		assertAll("Equality",
			() -> assertEquals(isTaut, se.binaryOperation(lit_0, lit_0, "==").getStatusCode()),
			() -> assertEquals(isTaut, se.binaryOperation(lit_1, lit_1, "=").getStatusCode()),
			() -> assertEquals(isTaut, se.binaryOperation(lit_0, lit_1, "!=").getStatusCode()),
			() -> assertEquals(isTaut, se.binaryOperation(lit_5, lit_1, "<>").getStatusCode()),
			() -> assertEquals(isUnsat, se.binaryOperation(lit_5, lit_1, "==").getStatusCode()),
			() -> assertEquals(isUnsat, se.binaryOperation(lit_1, lit_minus_1, "==").getStatusCode()),
			() -> assertEquals(isUnsat, se.binaryOperation(lit_0, lit_0, "!=").getStatusCode()),
			() -> assertEquals(isUnsat, se.binaryOperation(lit_1, lit_1, "<>").getStatusCode())
		);
		
		assertAll("Comparison",
			() -> assertEquals(isTaut, se.binaryOperation(lit_0, lit_1, "<").getStatusCode()),
			() -> assertEquals(isTaut, se.binaryOperation(lit_0, lit_1, "<=").getStatusCode()),
			() -> assertEquals(isUnsat, se.binaryOperation(lit_0, lit_1, ">").getStatusCode()),
			() -> assertEquals(isUnsat, se.binaryOperation(lit_0, lit_1, ">=").getStatusCode()),
			() -> assertEquals(isUnsat, se.binaryOperation(lit_0, lit_minus_1, "<").getStatusCode()),
			() -> assertEquals(isUnsat, se.binaryOperation(lit_0, lit_minus_1, "<=").getStatusCode()),
			() -> assertEquals(isTaut, se.binaryOperation(lit_0, lit_minus_1, ">").getStatusCode()),
			() -> assertEquals(isTaut, se.binaryOperation(lit_0, lit_minus_1, ">=").getStatusCode()),
			() -> assertEquals(isTaut, se.binaryOperation(lit_1, lit_1, ">=").getStatusCode()),
			() -> assertEquals(isTaut, se.binaryOperation(lit_1, lit_1, "<=").getStatusCode()),
			() -> assertEquals(isUnsat, se.binaryOperation(lit_1, lit_1, "<").getStatusCode()),
			() -> assertEquals(isUnsat, se.binaryOperation(lit_1, lit_1, ">").getStatusCode())
		);
	}

}
