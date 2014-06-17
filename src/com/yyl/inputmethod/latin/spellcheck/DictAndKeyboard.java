/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yyl.inputmethod.latin.spellcheck;

import com.yyl.inputmethod.keyboard.Keyboard;
import com.yyl.inputmethod.keyboard.KeyboardId;
import com.yyl.inputmethod.keyboard.KeyboardLayoutSet;
import com.yyl.inputmethod.keyboard.ProximityInfo;
import com.yyl.inputmethod.latin.Dictionary;

/**
 * A container for a Dictionary and a Keyboard.
 */
public final class DictAndKeyboard {
    public final Dictionary mDictionary;
    private final Keyboard mKeyboard;
    private final Keyboard mManualShiftedKeyboard;

    public DictAndKeyboard(
            final Dictionary dictionary, final KeyboardLayoutSet keyboardLayoutSet) {
        mDictionary = dictionary;
        if (keyboardLayoutSet == null) {
            mKeyboard = null;
            mManualShiftedKeyboard = null;
            return;
        }
        mKeyboard = keyboardLayoutSet.getKeyboard(KeyboardId.ELEMENT_ALPHABET);
        mManualShiftedKeyboard =
                keyboardLayoutSet.getKeyboard(KeyboardId.ELEMENT_ALPHABET_MANUAL_SHIFTED);
    }

    public Keyboard getKeyboard(final int codePoint) {
        if (mKeyboard == null) {
            return null;
        }
        return mKeyboard.getKey(codePoint) != null ? mKeyboard : mManualShiftedKeyboard;
    }

    public ProximityInfo getProximityInfo() {
        return mKeyboard == null ? null : mKeyboard.getProximityInfo();
    }
}
