/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package org.gedcom4j.validate;

/**
 * <p>
 * The interface for an AutoRepairResponder.
 * </p>
 * <p>
 * When the gedcom4j {@link Validator} class has a finding that it is able to automatically repair, it will call the
 * {@link #mayRepair(ValidationFinding)} method on the auto-repair validator that is registered in the {@link Validator} class. If
 * that implementation returns true, the auto-repair will proceed...otherwise, it won't.
 * </p>
 * <p>
 * Built-in implementations exist to allow everything ({@link Validator#AUTO_REPAIR_ALL}) and to allow nothing
 * ({@link Validator#AUTO_REPAIR_NONE}) to be auto-repaired. You can also write your own implementation if you want to be more
 * selective. You will need to implement this interface, and your implementation should examine the {@link ValidationFinding} passed
 * in on the callback to decide whether to return true (to permit the repair) or false (to forbid the repair).
 * </p>
 */
public interface AutoRepairResponder {

    /**
     * Determine whether or not the validate may repair the finding
     *
     * @param repairableValidationFinding
     *            the repairable validation finding
     * @return true if the validator is to be permitted to make the auto-repair, or return false if the repair is not to be
     *         performed.
     */
    boolean mayRepair(ValidationFinding repairableValidationFinding);
}
