package bo.gob.yguasu.modules.clientes.service;

import bo.gob.yguasu.common.exception.BadRequestException;
import bo.gob.yguasu.common.exception.ResourceNotFoundException;
import bo.gob.yguasu.modules.clientes.domain.Cliente;
import bo.gob.yguasu.modules.clientes.dto.ClienteDTO;
import bo.gob.yguasu.modules.clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public Page<Cliente> findAll(Pageable pageable) {
        return clienteRepository.findAllByEstado('A', pageable);
    }

    @Transactional(readOnly = true)
    public Page<Cliente> search(String search, Pageable pageable) {
        return clienteRepository.searchByEstado('A', search, pageable);
    }

    @Transactional(readOnly = true)
    public Cliente findById(Integer id) {
        return clienteRepository.findByIdUsuarioAndEstado(id, 'A')
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", id));
    }

    @Transactional
    public Cliente create(ClienteDTO dto, Integer idUsuario) {
        // Validaciones
        if (dto.getCi() != null && clienteRepository.existsByCi(dto.getCi())) {
            throw new BadRequestException("Ya existe un cliente con el CI: " + dto.getCi());
        }
        if (dto.getNit() != null && clienteRepository.existsByNit(dto.getNit())) {
            throw new BadRequestException("Ya existe un cliente con el NIT: " + dto.getNit());
        }

        Cliente cliente = Cliente.builder()
                .paterno(dto.getPaterno())
                .materno(dto.getMaterno())
                .nombres(dto.getNombres())
                .sexo(dto.getSexo())
                .ci(dto.getCi())
                .nit(dto.getNit())
                .fecNacimiento(dto.getFecNacimiento())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .fax(dto.getFax())
                .celular(dto.getCelular())
                .build();

        cliente.setIdUsuario(idUsuario);
        cliente.setEstado('A');

        return clienteRepository.save(cliente);
    }

    @Transactional
    public Cliente update(Integer id, ClienteDTO dto, Integer idUsuario) {
        Cliente cliente = findById(id);

        cliente.setPaterno(dto.getPaterno());
        cliente.setMaterno(dto.getMaterno());
        cliente.setNombres(dto.getNombres());
        cliente.setSexo(dto.getSexo());
        cliente.setCi(dto.getCi());
        cliente.setNit(dto.getNit());
        cliente.setFecNacimiento(dto.getFecNacimiento());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setFax(dto.getFax());
        cliente.setCelular(dto.getCelular());
        cliente.setIdUsuario(idUsuario);

        return clienteRepository.save(cliente);
    }

    @Transactional
    public void delete(Integer id, Integer idUsuario) {
        Cliente cliente = findById(id);
        cliente.setEstado('I');
        cliente.setIdUsuario(idUsuario);
        clienteRepository.save(cliente);
    }
}
