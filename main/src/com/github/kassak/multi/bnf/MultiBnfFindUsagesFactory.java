package com.github.kassak.multi.bnf;

import com.intellij.find.findUsages.*;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.psi.PsiElement;
import com.intellij.ui.StateRestoringCheckBox;
import com.intellij.util.containers.ContainerUtil;
import org.intellij.grammar.psi.BnfRule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

public class MultiBnfFindUsagesFactory extends FindUsagesHandlerFactory {
  @Override
  public boolean canFindUsages(@NotNull PsiElement psiElement) {
    return psiElement instanceof BnfRule;
  }

  @Nullable
  @Override
  public FindUsagesHandler createFindUsagesHandler(@NotNull PsiElement psiElement, boolean forHighlightUsages) {
    return new FindUsagesHandler(psiElement) {
      @NotNull
      @Override
      public PsiElement[] getSecondaryElements() {
        if (!MultiBnfUtils.isEnabled()) return PsiElement.EMPTY_ARRAY;
        BnfRule rule = (BnfRule)psiElement;
        List<BnfRule> res = ContainerUtil.newArrayList();
        for (BnfRule r : MultiBnfUtils.getRuleCluster(rule)) {
          if (r != rule) res.add(r);
        }
        return res.toArray(PsiElement.EMPTY_ARRAY);
      }

      @NotNull
      @Override
      public FindUsagesOptions getFindUsagesOptions() {
        return super.getFindUsagesOptions();
      }

      @NotNull
      @Override
      public AbstractFindUsagesDialog getFindUsagesDialog(boolean isSingleFile, boolean toShowInNewTab, boolean mustOpenInNewTab) {
        @SuppressWarnings("deprecation")
        DataContext ctx = DataManager.getInstance().getDataContext();
        FindUsagesOptions options = getFindUsagesOptions(ctx);
        return new CommonFindUsagesDialog(getPsiElement(), getProject(), options, toShowInNewTab, mustOpenInNewTab, isSingleFile, this) {
          private StateRestoringCheckBox myRelatedCb;

          @Override
          protected void addUsagesOptions(JPanel optionsPanel) {
            super.addUsagesOptions(optionsPanel);
            myRelatedCb = addCheckboxToPanel("Search usages of related rules", MultiBnfUtils.isEnabled(), optionsPanel, false);
          }

          @Override
          public void calcFindUsagesOptions(FindUsagesOptions options) {
            super.calcFindUsagesOptions(options);
            MultiBnfUtils.setEnabled(myRelatedCb.isSelected());
          }
        };
      }
    };
  }
}
