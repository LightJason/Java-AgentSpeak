// --- emergency car --------------------------------------------------------------------
// the emergency car drives on non-activation like a default car, but on random
// activation it send to the predecessor car a message for rerouting, so the agent can
// drive along the street faster
// --------------------------------------------------------------------------------------

emergency_activation(0.3).
