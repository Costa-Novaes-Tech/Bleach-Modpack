package com.bleachmod.common.visual;

import java.nio.file.Path;

/** Allows the same fixture in a locally packaged client without enabling development commands. */
public final class RyujinQaSandbox {
    private RyujinQaSandbox() {}

    public static boolean packaged(Path path) {
        return Boolean.getBoolean("bleachmod.packagedRyujinQa")
                && Boolean.getBoolean("bleachmod.ryujinBankaiQa")
                && Boolean.getBoolean("bleachmod.cinematicQaFast")
                && path.toAbsolutePath().normalize().toString().replace('\\','/')
                        .contains("/build/visual-lab/run-benihime-qa/");
    }
}
