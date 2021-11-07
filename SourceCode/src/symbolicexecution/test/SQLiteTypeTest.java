package symbolicexecution.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;

import symbolicexecution.sqlite.type.SQLiteTypeFactory;
import symbolicexecution.sqltype.SQLAnyType;
import symbolicexecution.sqltype.SQLInteger;
import symbolicexecution.sqltype.SQLTypeFactory;

class SQLiteTypeTest {

	private SQLTypeFactory factory;
	private Context z3context;
	private Solver solver;

	@BeforeEach
	void setUp() throws Exception {
		this.z3context = new Context();
		this.solver = this.z3context.mkSolver();
		this.factory = new SQLiteTypeFactory(this.z3context);
	}

	@AfterEach
	void tearDown() throws Exception {
		this.solver = null;
		this.z3context.close();
		this.factory = null;
	}

	@ParameterizedTest
	@DisplayName("Literal Integer Tests")
	@CsvSource({
		"0, 0",
		"5, 5",
		"-5, -5",
		"1, 0",
		"0, 1",
		"5, 13",
		"13, 5",
		"0, 5",
		"5, 0",
		"1, -1",
		"-1, 1",
		"0, -5",
		"-5, 0",
		"5, -13",
		"-5, 13",
		"-5, -13"
	})
	void literalIntegerTests(long a, long b) {
		SQLInteger v1 = factory.makeIntegerLiteral(a);
		SQLInteger v2 = factory.makeIntegerLiteral(b);
		
		assertTrue(isExactlyEqual(v1, a), () -> "Literal Value");
		assertTrue(isExactlyEqual(v1.not(), (a == 0) ? 1 : 0), () -> "Not");
		assertTrue(isExactlyEqual(v1.bitwiseNot(), ~a), () -> "Bitwise Not");
		assertTrue(isExactlyEqual(v1.negation(), -a), () -> "Negation");
		assertTrue(isExactlyEqual(v1.add(v2), a + b), () -> "Addition");
		assertTrue(isExactlyEqual(v1.sub(v2), a - b), () -> "Subtraction");
		assertTrue(isExactlyEqual(v1.mul(v2), a * b), () -> "Multiplication");
		if (b != 0)
			assertTrue(isExactlyEqual(v1.div(v2), a / b), () -> "Division");
		if (b > 1)
			assertTrue(isExactlyEqual(v1.mod(v2), a % b), () -> "Modulo");
		assertTrue(isExactlyEqual(v1.eq(v2), (a == b) ? 1 : 0), () -> "Equal");
		assertTrue(isExactlyEqual(v1.neq(v2), (a != b) ? 1 : 0), () -> "Not Equal");
		assertTrue(isExactlyEqual(v1.gt(v2), (a > b) ? 1 : 0), () -> ">");
		assertTrue(isExactlyEqual(v1.geq(v2), (a >= b) ? 1 : 0), () -> ">=");
		assertTrue(isExactlyEqual(v1.lt(v2), (a < b) ? 1 : 0), () -> "<");
		assertTrue(isExactlyEqual(v1.leq(v2), (a <= b) ? 1 : 0), () -> "<=");
		if (b >= 0) {
			assertTrue(isExactlyEqual(v1.lls(v2), a << b), () -> "Logical Left Shift");
			assertTrue(isExactlyEqual(v1.lrs(v2), a >> b), () -> "Logical Right Shift");
		}
		assertTrue(isExactlyEqual(v1.bitwiseAnd(v2), a & b), () -> "Bitwise And");
		assertTrue(isExactlyEqual(v1.bitwiseOr(v2), a | b), () -> "Bitwise Or");
	}
	
	
	private boolean isExactlyEqual(SQLAnyType integerToTest, long expectedValue) {
		IntExpr testExpr = integerToTest.convertToIntegerType().getIntExpr();
		IntExpr expectedExpr = z3context.mkInt(expectedValue);
		BoolExpr equalCheck = z3context.mkEq(testExpr, expectedExpr);
		BoolExpr anotherValuePossible = z3context.mkOr(z3context.mkGt(testExpr, expectedExpr), z3context.mkLt(testExpr, expectedExpr));
		Status status;
		status = solver.check(equalCheck);
		if (status != Status.SATISFIABLE)
			return false;
		status = solver.check(anotherValuePossible);
		if (status != Status.UNSATISFIABLE)
			return false;
		return true;
	}
	
}
