/**
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the Light-Jason                                               #
 * # Copyright (c) 2015-16, Philipp Kraus (philipp.kraus@tu-clausthal.de)               #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

/**
 * high-level grammar rules of AgentSpeak(L)
 * which defines the agent
 **/
grammar Agent;
import AgentSpeak;


// --- agent structure -------------------------------------------------------------------

/**
 * initial grammar rule
 **/
agent :
    initial_beliefs?
    initial_goal?
    logicrules?
    plans
    ;

/**
 * rule to represent initial beliefs
 **/
initial_beliefs :
    belief+
    ;

/**
 * rule to represent the initial goal
 **/
initial_goal :
    EXCLAMATIONMARK
    atom
    DOT
    ;

// ---------------------------------------------------------------------------------------
