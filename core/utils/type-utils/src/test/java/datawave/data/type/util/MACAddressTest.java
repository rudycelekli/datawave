package datawave.data.type.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class MACAddressTest {

    private static final String NORMALIZED = "00-12-34-56-78-90";

    @Test
    public void testParseStandardSeparators() {
        assertEquals(NORMALIZED, MACAddress.parse("00:12:34:56:78:90").toNormalizedString("-"));
        assertEquals(NORMALIZED, MACAddress.parse("00-12-34-56-78-90").toNormalizedString("-"));
        assertEquals(NORMALIZED, MACAddress.parse("0012:3456:7890").toNormalizedString("-"));
        assertEquals(NORMALIZED, MACAddress.parse("0012-3456-7890").toNormalizedString("-"));
    }

    /**
     * A dot is one of the standard MAC separators (e.g. the Cisco form), so an address whose digits all happen to be decimal must be accepted just as it is
     * with ':' or '-'.
     */
    @Test
    public void testParseDotSeparatedAddressWithOnlyDecimalDigits() {
        assertEquals(NORMALIZED, MACAddress.parse("0012.3456.7890").toNormalizedString("-"));
        assertEquals(NORMALIZED, MACAddress.parse("00.12.34.56.78.90").toNormalizedString("-"));
        assertEquals(NORMALIZED, MACAddress.parse("0012.3456.7890", ".").toNormalizedString("-"));
        assertEquals(NORMALIZED, MACAddress.parse("0012.3456.7890", 4).toNormalizedString("-"));
    }

    @Test
    public void testParseDotSeparatedAddressWithHexDigits() {
        assertEquals("00-1A-34-56-78-90", MACAddress.parse("001a.3456.7890").toNormalizedString("-"));
    }

    /**
     * Non-standard layouts that contain no hex letters are still rejected, so things like dotted-decimal addresses are not mistaken for MAC addresses.
     */
    @Test
    public void testNonStandardDecimalOnlyAddressIsStillRejected() {
        assertThrows(IllegalArgumentException.class, () -> MACAddress.parse("192.168.001.001"));
        assertThrows(IllegalArgumentException.class, () -> MACAddress.parse("123 456 789 012"));
    }
}
