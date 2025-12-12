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

  // const [formData, setFormData] = useState({
  //   name: "",
  //   email: "",
  //   password: "",
  //   confirmPassword: "",
  // });

  const [formData, setFormData] = useState({
    firstname: "", // Changed from 'name'
    lastname: "", // Changed from 'name'
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

    // if (!isLogin && !formData.name.trim()) {
    //   newErrors.name = "Le nom est requis";
    // }

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
      // Simuler un appel API
      await new Promise((resolve) => setTimeout(resolve, 1500));

      // TODO: Remplacer par un vrai appel API
      let responseData;
      if (isLogin) {
        const response = await authenticateUser({
          email: formData.email,
          password: formData.password,
        });
        responseData = response;
        console.log("Connexion réussie:", response);
      } else {
        const response = await registerUser({
          firstname: formData.firstname,
          lastname: formData.lastname,
          email: formData.email,
          password: formData.password,
        });
        console.log(formData);
        responseData = response;
        console.log("Registration réussie:", response);
      }

      // Stocker le token (exemple)
      localStorage.setItem("token", responseData.token);
      if (!isLogin) {
        localStorage.setItem("userName", formData.firstname || "Utilisateur");

        console.log(
          "New user registered:",
          formData.firstname,
          formData.lastname
        );
      } else {
        // For login, use response data if available
        localStorage.setItem(
          "userName",
          responseData.user?.firstname || "Utilisateur"
        );
        localStorage.setItem("role", responseData.user.role);
        console.log("Utilisateur role:", responseData.user?.role);
      }

      if (onSuccess) {
        onSuccess();
      }

      onClose();

      // Afficher un message de succès (tu peux utiliser ton composant Toast)
    } catch (error) {
      console.error("Erreur de connexion:", error);
      setErrors({ submit: "Erreur de connexion. Veuillez réessayer." });
    } finally {
      setLoading(false);
    }
  };

  // Basculer entre Login et Register
  // const toggleMode = () => {
  //   setIsLogin(!isLogin);
  //   setFormData({
  //     name: "",
  //     email: "",
  //     password: "",
  //     confirmPassword: "",
  //   });
  //   setErrors({});
  // };
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
          {/* Nom (seulement pour Register) */}
          {/* {!isLogin && (
            <>
              <div>
                <label className="block text-sm font-semibold text-gray-700 mb-2">
                  <User className="w-4 h-4 inline mr-2" />
                  Nom
                </label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  placeholder="Ex: Bennani"
                  className={`
                  w-full px-4 py-3 border rounded-lg
                  focus:outline-none focus:ring-2 focus:ring-primary
                  ${errors.name ? "border-red-500" : "border-gray-300"}
                `}
                />
                {errors.name && (
                  <p className="text-red-500 text-sm mt-1">{errors.name}</p>
                )}
              </div>
              <div>
                <label className="block text-sm font-semibold text-gray-700 mb-2">
                  <User className="w-4 h-4 inline mr-2" />
                  Prenom
                </label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  placeholder="Ex: Ahmed"
                  className={`
                  w-full px-4 py-3 border rounded-lg
                  focus:outline-none focus:ring-2 focus:ring-primary
                  ${errors.name ? "border-red-500" : "border-gray-300"}
                `}
                />
                {errors.name && (
                  <p className="text-red-500 text-sm mt-1">{errors.name}</p>
                )}
              </div>
            </>
          )} */}

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

          {/* Séparateur */}
          <div className="relative py-4">
            <div className="absolute inset-0 flex items-center">
              <div className="w-full border-t border-gray-300"></div>
            </div>
            <div className="relative flex justify-center text-sm">
              <span className="px-2 bg-white text-gray-500">
                Ou continuer avec
              </span>
            </div>
          </div>

          {/* Boutons sociaux (optionnel) */}
          <div className="grid grid-cols-2 gap-3">
            <button
              type="button"
              className="flex items-center justify-center gap-2 px-4 py-3 border border-gray-300 rounded-lg hover:bg-gray-50 transition"
            >
              <svg className="w-5 h-5" viewBox="0 0 24 24">
                <path
                  fill="#4285F4"
                  d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                />
                <path
                  fill="#34A853"
                  d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                />
                <path
                  fill="#FBBC05"
                  d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
                />
                <path
                  fill="#EA4335"
                  d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                />
              </svg>
              Google
            </button>
            <button
              type="button"
              className="flex items-center justify-center gap-2 px-4 py-3 border border-gray-300 rounded-lg hover:bg-gray-50 transition"
            >
              <svg className="w-5 h-5" fill="#1877F2" viewBox="0 0 24 24">
                <path d="M24 12.073c0-6.627-5.373-12-12-12s-12 5.373-12 12c0 5.99 4.388 10.954 10.125 11.854v-8.385H7.078v-3.47h3.047V9.43c0-3.007 1.792-4.669 4.533-4.669 1.312 0 2.686.235 2.686.235v2.953H15.83c-1.491 0-1.956.925-1.956 1.874v2.25h3.328l-.532 3.47h-2.796v8.385C19.612 23.027 24 18.062 24 12.073z" />
              </svg>
              Facebook
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default LoginModal;
