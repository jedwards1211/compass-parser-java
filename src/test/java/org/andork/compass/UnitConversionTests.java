package org.andork.compass;

import org.junit.Assert;
import org.junit.Test;

public class UnitConversionTests {
	@Test
	public void testAzimuthConversions() {
		Assert.assertEquals(200.0, AzimuthUnit.convert(180.0, AzimuthUnit.GRADS), 0.0);
		Assert.assertEquals(1.0, AzimuthUnit.convert(1.0, AzimuthUnit.QUADS), 0.0);
		Assert.assertEquals(1.0, AzimuthUnit.convert(1.0, AzimuthUnit.DEGREES), 0.0);
	}

	@Test
	public void testLengthConversions() {
		Assert.assertEquals(0.3048, LengthUnit.convert(1.0, LengthUnit.METERS), 0.0);
		Assert.assertEquals(1.0, LengthUnit.convert(1.0, LengthUnit.FEET_AND_INCHES), 0.0);
		Assert.assertEquals(1.0, LengthUnit.convert(1.0, LengthUnit.DECIMAL_FEET), 0.0);
	}
}
