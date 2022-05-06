package com.usuario.service.Service;

import com.usuario.service.Models.Carro;
import com.usuario.service.Models.Moto;
import com.usuario.service.Repository.UsuarioRepository;
import com.usuario.service.entitys.Usuario;
import com.usuario.service.feingClients.CarroFeingClient;
import com.usuario.service.feingClients.MotoFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Autowired
    private CarroFeingClient carroFeingClient;

    @Autowired
    private MotoFeignClient motoFeignClient;


    public List<Carro> getCarros(int usuarioID){
        List<Carro> carros = restTemplate.getForObject("http://localhost:8002/carro/usuario/"+ usuarioID,List.class);
        return carros;
    }
    public List<Moto> getMotos(int usuarioID){
        List<Moto> motos = restTemplate.getForObject("http://localhost:8003/moto/usuario/"+ usuarioID,List.class);
        return motos;
    }

    public List<Usuario> gettAll(){
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioByID(int id){
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario save(Usuario usuario){
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return nuevoUsuario;
    }

    public Carro saveCarro(int usuarioID,Carro carro){
        carro.setUsuarioId(usuarioID);
        Carro nuevoCarro = carroFeingClient.save(carro);
        return nuevoCarro;
    }

    public Moto saveMoto(int usuarioID,Moto moto){
        moto.setUsuarioId(usuarioID);
        Moto nuevoMoto = motoFeignClient.save(moto);
        return nuevoMoto;
    }

    public Map<String, Object> getUsuarioAndVehiculos(int usuarioId){
        Map<String,Object> resultado = new HashMap<>();
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if(usuario == null) {
            resultado.put("Mensaje", "El usuario no existe");
            return resultado;
        }

        resultado.put("Usuario",usuario);
        List<Carro> carros = carroFeingClient.getCarros(usuarioId);
        if(carros.isEmpty()) {
            resultado.put("Carros", "El usuario no tiene carros");
        }
        else {
            resultado.put("Carros", carros);
        }

        List<Moto> motos = motoFeignClient.getMotos(usuarioId);
        if(motos.isEmpty()) {
            resultado.put("Motos", "El usuario no tiene motos");
        }
        else {
            resultado.put("Motos", motos);
        }
        return resultado;
    }
}
