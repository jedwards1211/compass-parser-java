package org.andork.compass;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class UnitConversionTests {
	@Test
	public void testAzimuthConversions() {
		Assert.assertEquals(200.0, AzimuthUnit.convert(new BigDecimal(180.0), AzimuthUnit.GRADS).doubleValue(), 0.0);
		Assert.assertEquals(1.0, AzimuthUnit.convert(BigDecimal.ONE, AzimuthUnit.QUADS).doubleValue(), 0.0);
		Assert.assertEquals(1.0, AzimuthUnit.convert(BigDecimal.ONE, AzimuthUnit.DEGREES).doubleValue(), 0.0);
	}

	@Test
	public void testInclinationConversions() {
		Assert.assertEquals(100,
				InclinationUnit.convert(new BigDecimal(45), InclinationUnit.PERCENT_GRADE).doubleValue(), 1e-10);
		Assert.assertEquals(200, InclinationUnit.convert(new BigDecimal(180), InclinationUnit.GRADS).doubleValue(),
				0.0);
		Assert.assertEquals(180, InclinationUnit.convert(new BigDecimal(180), InclinationUnit.DEGREES).doubleValue(),
				0.0);
		Assert.assertEquals(180,
				InclinationUnit.convert(new BigDecimal(180), InclinationUnit.DEGREES_AND_MINUTES).doubleValue(), 0.0);
		Assert.assertEquals(180,
				InclinationUnit.convert(new BigDecimal(180), InclinationUnit.DEPTH_GAUGE).doubleValue(), 0.0);
	}

	@Test
	public void testLengthConversions() {
		Assert.assertEquals(0.3048, LengthUnit.convert(BigDecimal.ONE, LengthUnit.METERS).doubleValue(), 0.0);
		Assert.assertEquals(1.0, LengthUnit.convert(BigDecimal.ONE, LengthUnit.FEET_AND_INCHES).doubleValue(), 0.0);
		Assert.assertEquals(1.0, LengthUnit.convert(BigDecimal.ONE, LengthUnit.DECIMAL_FEET).doubleValue(), 0.0);
	}
}
