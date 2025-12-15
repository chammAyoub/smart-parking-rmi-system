import React, { useState } from "react";
import { X, Mail, Lock, Eye, EyeOff, User, LogIn } from "lucide-react";
import { authenticateUser, registerUser } from "../services/apiService";

/**
 * Modal de connexion / inscription
 *
 * @author Ayoub - Frontend Lead
 */
const LoginModal = ({ isOpen, onClose, onSuccess }) => {
  const [isLogin, setIsLogin] = useState(true); // true = Login, false = Register
  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);

  const [formData, setFormData] = useState({
    firstname: "", 
    lastname: "", 
    email: "",
    password: "",
    confirmPassword: "",
  });

  const [errors, setErrors] = useState({});

  if (!isOpen) return null;

  // Gérer les changements de champs
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    // Effacer l'erreur du champ modifié
    if (errors[name]) {
      setErrors((prev) => ({
        ...prev,
        [name]: "",
      }));
    }
  };

  // Validation du formulaire
  const validateForm = () => {
    const newErrors = {};

    if (!isLogin && !formData.firstname.trim()) {
      newErrors.firstname = "Le prénom est requis";
    }
    if (!isLogin && !formData.lastname.trim()) {
      newErrors.lastname = "Le nom est requis";
    }

    if (!formData.email.trim()) {
      newErrors.email = "L'email est requis";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = "Email invalide";
    }

    if (!formData.password) {
      newErrors.password = "Le mot de passe est requis";
    } else if (formData.password.length < 6) {
      newErrors.password =
        "Le mot de passe doit contenir au moins 6 caractères";
    }

    if (!isLogin) {
      if (!formData.confirmPassword) {
        newErrors.confirmPassword = "Confirmez votre mot de passe";
      } else if (formData.password !== formData.confirmPassword) {
        newErrors.confirmPassword = "Les mots de passe ne correspondent pas";
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  // Soumettre le formulaire
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setLoading(true);

    try {
      let responseData;
      
      if (isLogin) {
        // --- LOGIN ---
        const response = await authenticateUser({
          email: formData.email,
          password: formData.password,
        });
        responseData = response;
        console.log("Connexion réussie:", response);
      } else {
        // --- REGISTER ---
        const response = await registerUser({
          firstname: formData.firstname,
          lastname: formData.lastname,
          email: formData.email,
          password: formData.password,
        });
        responseData = response;
        console.log("Registration réussie:", response);
      }

      // =========================================================
      // SAUVEGARDER L'EMAIL DANS LOCALSTORAGE
      // =========================================================
      
      // 1. Stocker le token
      localStorage.setItem("token", responseData.token);

      // 2. Stocker l'Email (Pour filtrer l'historique des réservations)
      // Si on vient de Login, l'email est dans responseData.user.email
      // Si on vient de Register, l'email est dans formData.email
      const emailToSave = responseData.user?.email || formData.email;
      localStorage.setItem("userEmail", emailToSave);

      // 3. Stocker Nom et Role
      if (!isLogin) {
        // Cas Register
        localStorage.setItem("userName", formData.firstname || "Utilisateur");
      } else {
        // Cas Login
        localStorage.setItem("userName", responseData.user?.firstname || "Utilisateur");
        // Optionnel : Stocker le rôle si besoin
        if (responseData.user?.role) {
            localStorage.setItem("role", responseData.user.role);
        }
      }

      console.log("💾 User stored in LocalStorage:", {
          email: emailToSave,
          name: localStorage.getItem("userName")
      });

      if (onSuccess) {
        onSuccess();
      }

      onClose();

    } catch (error) {
      console.error("Erreur de connexion:", error);
      setErrors({ submit: "Erreur de connexion. Vérifiez vos identifiants." });
    } finally {
      setLoading(false);
    }
  };

  const toggleMode = () => {
    setIsLogin(!isLogin);
    setFormData({
      firstname: "",
      lastname: "",
      email: "",
      password: "",
      confirmPassword: "",
    });
    setErrors({});
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm p-4">
      <div className="bg-white rounded-2xl shadow-2xl max-w-md w-full max-h-[90vh] overflow-y-auto animate-fade-in">
        {/* Header */}
        <div className="sticky top-0 bg-gradient-to-r from-primary to-secondary text-white p-6 rounded-t-2xl">
          <div className="flex justify-between items-center">
            <div>
              <h2 className="text-2xl font-bold mb-1">
                {isLogin ? "Connexion" : "Créer un compte"}
              </h2>
              <p className="text-white/90 text-sm">
                {isLogin
                  ? "Connectez-vous pour gérer vos réservations"
                  : "Rejoignez Smart Parking aujourd'hui"}
              </p>
            </div>
            <button
              onClick={onClose}
              className="bg-white/20 hover:bg-white/30 rounded-full p-2 transition"
              disabled={loading}
            >
              <X className="w-6 h-6" />
            </button>
          </div>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 space-y-4">
          
          {!isLogin && (
            <>
              <div>
                <label className="block text-sm font-semibold text-gray-700 mb-2">
                  <User className="w-4 h-4 inline mr-2" />
                  Prénom
                </label>
                <input
                  type="text"
                  name="firstname"
                  value={formData.firstname}
                  onChange={handleChange}
                  placeholder="Ex: Ahmed"
                  className={`
                  w-full px-4 py-3 border rounded-lg
                  focus:outline-none focus:ring-2 focus:ring-primary
                  ${errors.firstname ? "border-red-500" : "border-gray-300"}
                `}
                />
                {errors.firstname && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.firstname}
                  </p>
                )}
              </div>
              <div>
                <label className="block text-sm font-semibold text-gray-700 mb-2">
                  <User className="w-4 h-4 inline mr-2" />
                  Nom
                </label>
                <input
                  type="text"
                  name="lastname"
                  value={formData.lastname}
                  onChange={handleChange}
                  placeholder="Ex: Bennani"
                  className={`
                  w-full px-4 py-3 border rounded-lg
                  focus:outline-none focus:ring-2 focus:ring-primary
                  ${errors.lastname ? "border-red-500" : "border-gray-300"}
                `}
                />
                {errors.lastname && (
                  <p className="text-red-500 text-sm mt-1">{errors.lastname}</p>
                )}
              </div>
            </>
          )}

          {/* Email */}
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              <Mail className="w-4 h-4 inline mr-2" />
              Email *
            </label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              placeholder="votre.email@exemple.com"
              className={`
                w-full px-4 py-3 border rounded-lg
                focus:outline-none focus:ring-2 focus:ring-primary
                ${errors.email ? "border-red-500" : "border-gray-300"}
              `}
            />
            {errors.email && (
              <p className="text-red-500 text-sm mt-1">{errors.email}</p>
            )}
          </div>

          {/* Mot de passe */}
          <div>
            <label className="block text-sm font-semibold text-gray-700 mb-2">
              <Lock className="w-4 h-4 inline mr-2" />
              Mot de passe *
            </label>
            <div className="relative">
              <input
                type={showPassword ? "text" : "password"}
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="••••••••"
                className={`
                  w-full px-4 py-3 pr-12 border rounded-lg
                  focus:outline-none focus:ring-2 focus:ring-primary
                  ${errors.password ? "border-red-500" : "border-gray-300"}
                `}
              />
              <button
                type="button"
                onClick={() => setShowPassword(!showPassword)}
                className="absolute right-4 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
              >
                {showPassword ? (
                  <EyeOff className="w-5 h-5" />
                ) : (
                  <Eye className="w-5 h-5" />
                )}
              </button>
            </div>
            {errors.password && (
              <p className="text-red-500 text-sm mt-1">{errors.password}</p>
            )}
          </div>

          {/* Confirmer mot de passe (seulement pour Register) */}
          {!isLogin && (
            <div>
              <label className="block text-sm font-semibold text-gray-700 mb-2">
                <Lock className="w-4 h-4 inline mr-2" />
                Confirmer le mot de passe *
              </label>
              <input
                type={showPassword ? "text" : "password"}
                name="confirmPassword"
                value={formData.confirmPassword}
                onChange={handleChange}
                placeholder="••••••••"
                className={`
                  w-full px-4 py-3 border rounded-lg
                  focus:outline-none focus:ring-2 focus:ring-primary
                  ${
                    errors.confirmPassword
                      ? "border-red-500"
                      : "border-gray-300"
                  }
                `}
              />
              {errors.confirmPassword && (
                <p className="text-red-500 text-sm mt-1">
                  {errors.confirmPassword}
                </p>
              )}
            </div>
          )}

          {/* Erreur de soumission */}
          {errors.submit && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg">
              {errors.submit}
            </div>
          )}

          {/* Mot de passe oublié (seulement pour Login) */}
          {isLogin && (
            <div className="text-right">
              <button
                type="button"
                className="text-sm text-primary hover:text-secondary transition"
              >
                Mot de passe oublié ?
              </button>
            </div>
          )}

          {/* Bouton Submit */}
          <button
            type="submit"
            disabled={loading}
            className={`
              w-full font-semibold py-3 px-6 rounded-lg transition flex items-center justify-center gap-2
              ${
                loading
                  ? "bg-gray-400 cursor-not-allowed"
                  : "bg-gradient-to-r from-primary to-secondary hover:opacity-90 text-white"
              }
            `}
          >
            {loading ? (
              <>
                <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin" />
                Chargement...
              </>
            ) : (
              <>
                <LogIn className="w-5 h-5" />
                {isLogin ? "Se connecter" : "Créer mon compte"}
              </>
            )}
          </button>

          {/* Toggle Login/Register */}
          <div className="text-center pt-4 border-t">
            <p className="text-gray-600">
              {isLogin ? "Pas encore de compte ?" : "Déjà un compte ?"}{" "}
              <button
                type="button"
                onClick={toggleMode}
                className="text-primary hover:text-secondary font-semibold transition"
              >
                {isLogin ? "Créer un compte" : "Se connecter"}
              </button>
            </p>
          </div>

        </form>
      </div>
    </div>
  );
};

export default LoginModal;