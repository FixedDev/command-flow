/*
 * This file is part of commandflow, licensed under the MIT license
 *
 * Copyright (c) 2020-2023 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.unnamed.commandflow.translator;

import team.unnamed.commandflow.Namespace;

import java.util.HashMap;
import java.util.Map;

public class DefaultMapTranslationProvider implements TranslationProvider {

    protected Map<String, String> translations;

    public DefaultMapTranslationProvider() {
        translations = new HashMap<>();
        translations.put("command.subcommand.invalid", "The subcommand %s doesn't exist!");
        translations.put("command.no-permission", "No permission.");
        translations.put("argument.no-more","No more arguments were found, size: %s position: %s");
        translations.put("number.out-range", "The number %s is not within the range min: %s max: %s");
        translations.put("invalid.byte", "The number %s is not a valid byte!");
        translations.put("invalid.integer", "The number %s is not a valid integer!");
        translations.put("invalid.float", "The number %s is not a valid float!");
        translations.put("invalid.double", "The number %s is not a valid double!");
        translations.put("invalid.boolean", "The string %s is not a valid boolean!");
        translations.put("invalid.enum-value", "The value %s is not valid, the valid values are: %s");
        translations.put("invalid.long", "The number %s is not a valid long!");
    }

    public String getTranslation(String key) {
        return translations.get(key);
    }

    @Override
    public String getTranslation(Namespace namespace, String key) {
        return getTranslation(key);
    }

}
