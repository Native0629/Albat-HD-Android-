/**
 * Copyright 2014 AnjLab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.albat.mobachir.billing;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.ref.WeakReference;

class BillingBase {

	private WeakReference<Context> contextReference;

	public BillingBase(Context context) {
		contextReference = new WeakReference<Context>(context);
	}

	public Context getContext() {
		return contextReference.get();
	}

	protected String getPreferencesBaseKey() {
		return contextReference.get().getPackageName() + "_preferences";
	}

	private SharedPreferences getPreferences() {
		if (contextReference.get() != null)
			return PreferenceManager.getDefaultSharedPreferences(contextReference.get());
		return null;
	}

	public void release() {
		if (contextReference != null)
			contextReference.clear();
	}

	protected boolean saveString(String key, String value) {
		SharedPreferences sp = getPreferences();
		if (sp != null) {
			SharedPreferences.Editor spe = sp.edit();
			spe.putString(key, value);
			spe.commit();
			return true;
		}
		return false;
	}

	protected String loadString(String key, String defValue) {
		SharedPreferences sp = getPreferences();
		if (sp != null)
			return sp.getString(key, defValue);
		return defValue;
	}

	protected boolean saveBoolean(String key, Boolean value) {
		SharedPreferences sp = getPreferences();
		if (sp != null) {
			SharedPreferences.Editor spe = sp.edit();
			spe.putBoolean(key, value);
			spe.commit();
			return true;
		}
		return false;
	}

	protected boolean loadBoolean(String key, boolean defValue) {
		SharedPreferences sp = getPreferences();
		if (sp != null)
			return sp.getBoolean(key, defValue);
		return defValue;
	}
}

