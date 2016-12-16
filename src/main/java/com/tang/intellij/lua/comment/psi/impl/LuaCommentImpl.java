package com.tang.intellij.lua.comment.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LazyParseablePsiElement;
import com.intellij.psi.tree.IElementType;
import com.tang.intellij.lua.comment.LuaCommentUtil;
import com.tang.intellij.lua.comment.psi.LuaDocClassDef;
import com.tang.intellij.lua.comment.psi.LuaDocParamDef;
import com.tang.intellij.lua.comment.psi.LuaDocParamNameRef;
import com.tang.intellij.lua.comment.psi.LuaDocTypeDef;
import com.tang.intellij.lua.comment.psi.api.LuaComment;
import com.tang.intellij.lua.lang.type.LuaTypeSet;
import com.tang.intellij.lua.psi.LuaCommentOwner;
import com.tang.intellij.lua.psi.LuaTypes;

/**
 * Created by Tangzx on 2016/11/21.
 *
 * @qq 272669294
 */
public class LuaCommentImpl extends LazyParseablePsiElement implements LuaComment {

    public LuaCommentImpl(CharSequence charSequence) {
        super(LuaTypes.DOC_COMMENT, charSequence);
    }

    @Override
    public IElementType getTokenType() {
        return LuaTypes.DOC_COMMENT;
    }

    @Override
    public LuaCommentOwner getOwner() {
        return LuaCommentUtil.findOwner(this);
    }

    @Override
    public LuaDocParamDef getParamDef(String name) {
        PsiElement element = getFirstChild();
        while (element != null) {
            if (element instanceof LuaDocParamDef) {
                LuaDocParamDef paramDef = (LuaDocParamDef) element;
                LuaDocParamNameRef nameRef = paramDef.getParamNameRef();
                if (nameRef != null && nameRef.getText().equals(name))
                    return paramDef;
            }
            element = element.getNextSibling();
        }
        return null;
    }

    @Override
    public LuaDocClassDef getClassDef() {
        PsiElement element = getFirstChild();
        while (element != null) {
            if (element instanceof LuaDocClassDef) {
                return (LuaDocClassDef) element;
            }
            element = element.getNextSibling();
        }
        return null;
    }

    @Override
    public LuaDocTypeDef getTypeDef() {
        PsiElement element = getFirstChild();
        while (element != null) {
            if (element instanceof LuaDocTypeDef) {
                return (LuaDocTypeDef) element;
            }
            element = element.getNextSibling();
        }
        return null;
    }

    @Override
    public LuaTypeSet guessType() {
        LuaDocClassDef classDef = getClassDef();
        if (classDef != null)
            return LuaTypeSet.create(classDef);
        LuaDocTypeDef typeDef = getTypeDef();
        if (typeDef != null)
            return typeDef.guessType();
        return null;
    }

    @Override
    public String toString() {
        return "DOC_COMMENT";
    }
}