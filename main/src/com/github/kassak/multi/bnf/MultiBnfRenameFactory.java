package com.github.kassak.multi.bnf;

import com.intellij.psi.PsiElement;
import com.intellij.refactoring.rename.naming.AutomaticRenamer;
import com.intellij.refactoring.rename.naming.AutomaticRenamerFactory;
import com.intellij.usageView.UsageInfo;
import org.intellij.grammar.psi.BnfRule;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class MultiBnfRenameFactory implements AutomaticRenamerFactory {
  @Override
  public boolean isApplicable(@NotNull PsiElement psiElement) {
    return psiElement instanceof BnfRule;
  }

  @Nls
  @Nullable
  @Override
  public String getOptionName() {
    return "Rename related rules";
  }

  @Override
  public boolean isEnabled() {
    return MultiBnfUtils.isEnabled();
  }

  @Override
  public void setEnabled(boolean enabled) {
    MultiBnfUtils.setEnabled(enabled);
  }

  @NotNull
  @Override
  public AutomaticRenamer createRenamer(PsiElement psiElement, String newName, Collection<UsageInfo> collection) {
    return new AutomaticRenamer() {
      {
        BnfRule rule = (BnfRule)psiElement;
        for (BnfRule r : MultiBnfUtils.getRuleCluster(rule)) {
          if (r != rule) myElements.add(r);
        }
        suggestAllNames(rule.getName(), newName);
      }

      @Nls
      @Override
      public String getDialogTitle() {
        return "Rename related rules";
      }

      @Nls
      @Override
      public String getDialogDescription() {
        return null;
      }

      @Override
      public String entityName() {
        return "Related rule";
      }
    };
  }
}
