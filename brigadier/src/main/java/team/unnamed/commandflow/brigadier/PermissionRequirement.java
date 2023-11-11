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
package team.unnamed.commandflow.brigadier;

import team.unnamed.commandflow.Authorizer;
import team.unnamed.commandflow.Namespace;
import team.unnamed.commandflow.bukkit.BukkitCommandManager;
import me.lucko.commodore.Commodore;
import org.bukkit.command.CommandSender;

import java.util.function.Predicate;

public class PermissionRequirement implements Predicate<Object> {

    private final String permission;
    private final Authorizer authorizer;
    private final Commodore commodore;

    public PermissionRequirement(String permission, Authorizer authorizer, Commodore commodore) {
        this.permission = permission;
        this.authorizer = authorizer;
        this.commodore = commodore;
    }

    @Override
    public boolean test(Object o) {
        CommandSender sender = commodore.getBukkitSender(o);

        Namespace namespace = Namespace.create();
        namespace.setObject(CommandSender.class, BukkitCommandManager.SENDER_NAMESPACE, sender);

        return authorizer.isAuthorized(namespace, permission);
    }

}
