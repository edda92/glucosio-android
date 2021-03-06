/*
 * Copyright (C) 2016 Glucosio Foundation
 *
 * This file is part of Glucosio.
 *
 * Glucosio is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * Glucosio is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Glucosio.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package org.glucosio.android.presenter;

import org.glucosio.android.db.DatabaseHandler;
import org.glucosio.android.fragment.AssistantFragment;

public class AssistantPresenter {
    private DatabaseHandler dB;
    private AssistantFragment fragment;


    public AssistantPresenter(AssistantFragment assistantFragment) {
        this.fragment = assistantFragment;
        dB = new DatabaseHandler(assistantFragment.getContext());
    }

    public void addReading() {
        fragment.addReading();
    }

    public void startExportActivity() {
        fragment.startExportActivity();
    }

    public void startA1CCalculatorActivity() {
        fragment.startA1CCalculatorActivity();
    }

    public void openLiveChat() {
        fragment.openLiveChat();
    }
}
