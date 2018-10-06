package com.github.kassak.multi.bnf;

import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.containers.JBIterable;
import org.intellij.grammar.psi.BnfFile;
import org.intellij.grammar.psi.BnfRule;
import org.jetbrains.annotations.NotNull;

public class MultiBnfUtils {
  @NotNull
  public static Iterable<? extends BnfFile> getFileCluster(@NotNull BnfFile file) {
    return ContainerUtil.mapNotNull(
      file.getContainingDirectory().getFiles(),
      f -> ObjectUtils.tryCast(f, BnfFile.class)
    );
  }

  @NotNull
  public static Iterable<? extends BnfRule> getRuleCluster(@NotNull BnfRule rule) {
    String name = rule.getName();
    return JBIterable.from(getFileCluster((BnfFile)rule.getContainingFile()))
      .filterMap(f -> f.getRule(name));
  }
}
