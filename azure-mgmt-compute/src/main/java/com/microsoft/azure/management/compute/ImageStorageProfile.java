/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.compute;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Describes a storage profile.
 */
public class ImageStorageProfile {
    /**
     * Specifies information about the operating system disk used by the
     * virtual machine. &lt;br&gt;&lt;br&gt; For more information about disks,
     * see [About disks and VHDs for Azure virtual
     * machines](https://docs.microsoft.com/azure/virtual-machines/virtual-machines-windows-about-disks-vhds?toc=%2fazure%2fvirtual-machines%2fwindows%2ftoc.json).
     */
    @JsonProperty(value = "osDisk", required = true)
    private ImageOSDisk osDisk;

    /**
     * Specifies the parameters that are used to add a data disk to a virtual
     * machine. &lt;br&gt;&lt;br&gt; For more information about disks, see
     * [About disks and VHDs for Azure virtual
     * machines](https://docs.microsoft.com/azure/virtual-machines/virtual-machines-windows-about-disks-vhds?toc=%2fazure%2fvirtual-machines%2fwindows%2ftoc.json).
     */
    @JsonProperty(value = "dataDisks")
    private List<ImageDataDisk> dataDisks;

    /**
     * Get the osDisk value.
     *
     * @return the osDisk value
     */
    public ImageOSDisk osDisk() {
        return this.osDisk;
    }

    /**
     * Set the osDisk value.
     *
     * @param osDisk the osDisk value to set
     * @return the ImageStorageProfile object itself.
     */
    public ImageStorageProfile withOsDisk(ImageOSDisk osDisk) {
        this.osDisk = osDisk;
        return this;
    }

    /**
     * Get the dataDisks value.
     *
     * @return the dataDisks value
     */
    public List<ImageDataDisk> dataDisks() {
        return this.dataDisks;
    }

    /**
     * Set the dataDisks value.
     *
     * @param dataDisks the dataDisks value to set
     * @return the ImageStorageProfile object itself.
     */
    public ImageStorageProfile withDataDisks(List<ImageDataDisk> dataDisks) {
        this.dataDisks = dataDisks;
        return this;
    }

}
